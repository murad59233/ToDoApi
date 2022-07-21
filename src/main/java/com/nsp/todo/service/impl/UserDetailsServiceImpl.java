package com.nsp.todo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.*;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.*;
import com.nsp.todo.model.dto.UserDto;
import com.nsp.todo.repository.*;
import com.nsp.todo.service.CvService;
import com.nsp.todo.service.MailService;
import com.nsp.todo.service.UserService;
import com.nsp.todo.validation.ValidatorService;
import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ValidatorService validatorService;
    private final LanguageRepo languageRepo;
    private final TechnologyRepo technologyRepo;
    private final CityRepo cityRepo;
    private final CountryRepo countryRepo;
    private final TokenRepo tokenRepo;
    private final JavaMailSender mailSender;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final MapperService mapperService;
    private final AddressRepo addressRepo;
    private final CvService cvService;
    private final UserService userService;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(()->new UserException(Status.USER_NOT_FOUND));
        return user;
    }

    @SneakyThrows
    @Transactional
    public void register(String user, MultipartFile multipartFile) {
        UserDto userDto = objectMapper.readValue(user, UserDto.class);
        validatorService.validate(userDto, AdvancedInfo.class);
        validatorService.validate(userDto.getAddress(), AdvancedInfo.class);
        validatorService.validate(userDto.getAddress().getCountry(), AdvancedInfo.class);
        validatorService.validate(userDto.getAddress().getCity(), AdvancedInfo.class);
        User updatedUser = mapperService.updateObject(userDto, new User());
        User u = modelMapper.map(updatedUser,User.class);
        if(userDto.getLanguages().isEmpty())
            throw new LanguageException(Status.LANGUAGE_NOT_FOUND);
        if(userDto.getTechnology().isEmpty())
            throw new TechnologyException(Status.TECHNOLOGY_NOT_FOUND);
        if(userRepo.findByEmail(u.getEmail()).isPresent())
            throw new UserException(Status.EMAIL_EXISTS);
        if(userRepo.findByUsername(u.getUsername()).isPresent())
            throw new UserException(Status.USERNAME_EXISTS);
        if(!countryRepo.existsById(userDto.getAddress().getCountry().getId()))
            throw new CountryException(Status.COUNTRY_NOT_FOUND);
        if(!cityRepo.existsById(userDto.getAddress().getCity().getId()))
            throw new CityException(Status.CITY_NOT_FOUND);
        if(cityRepo.findById(userDto.getAddress().getCity().getId()).get().getCountry().getId() != userDto.getAddress().getCountry().getId())
            throw new CityException(Status.CITY_NOT_EXIST_IN_COUNTRY);
        Set<Language> languages = userDto.getLanguages().stream().map(l->{
            if(!languageRepo.existsById(l.getId()))
                try {
                    throw new LanguageException(Status.LANGUAGE_NOT_FOUND);
                } catch (LanguageException e) {
                    throw new RuntimeException(e);
                }
            return modelMapper.map(l,Language.class);
        }).collect(Collectors.toSet());
        Set<Technology> technology = userDto.getTechnology().stream().map(t->{
            if(!technologyRepo.existsById(t.getId()))
                try {
                    throw new TechnologyException(Status.TECHNOLOGY_NOT_FOUND);
                } catch (TechnologyException e) {
                    throw new RuntimeException(e);
                }
            return modelMapper.map(t, Technology.class);
        }).collect(Collectors.toSet());
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setLanguages(languages);
        u.setTechnology(technology);
        userService.deleteDisableUserByEmailOrUsername(u.getUsername(),u.getEmail(),false);
        User savedUser = userRepo.save(u);
        cvService.uploadCv(savedUser,multipartFile);
        setAddress(userDto,savedUser);
        Token token = new Token(savedUser);
        Token savedToken = tokenRepo.save(token);
        sendConfirmMail(savedToken);

    }


    private void setAddress(UserDto userDto,User user){
        Address address = new Address();
        address.setAddress(userDto.getAddress().getAddress());
        address.setZipCode(userDto.getAddress().getZipCode());
        Country country = new Country();
        City city = new City();
        country.setId(userDto.getAddress().getCountry().getId());
        city.setId(userDto.getAddress().getCity().getId());
        address.setCountry(country);
        address.setCity(city);
        address.setUser(user);
        addressRepo.save(address);
    }

    @SneakyThrows
    public void confirmUser(String token){
        Optional<Token> optionalToken = tokenRepo.findByToken(token);
        if(!optionalToken.isPresent())
            throw new TokenException(Status.TOKEN_NOT_FOUND);
        if(optionalToken.get().getExpiredDate().before(new Date()))
            throw new TokenException(Status.TOKEN_HAS_EXPIRED);
        userRepo.enableUser(optionalToken.get().getUser().getId(), true);
        tokenRepo.deleteById(optionalToken.get().getId());
    }

    @SneakyThrows
    public void sendConfirmMail(Token token){
        String from = "NSP";
        String to = token.getEmail();
        String subject = "Confirm your email address";
        String link = "http://localhost:4000/register/confirm/"+token.getToken();
        String body = "<p>Hi, "+token.getUser().getName()+"<br><br>Please click <a href='"+link+"'>here</a>" +
                " and confirm your email address";
        mailService.sendMail(from,to,subject,body);
    }

}
