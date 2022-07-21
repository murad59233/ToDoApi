package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.*;
import com.nsp.todo.mapper.MapperService;
import com.nsp.todo.model.*;
import com.nsp.todo.model.dto.UserDto;
import com.nsp.todo.model.response.*;
import com.nsp.todo.repository.*;
import com.nsp.todo.service.CvService;
import com.nsp.todo.service.MailService;
import com.nsp.todo.service.UserService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper mapper;
    private final MapperService mapperService;
    private final CountryRepo countryRepo;
    private final CityRepo cityRepo;
    private final LanguageRepo languageRepo;
    private final TechnologyRepo technologyRepo;
    private final JavaMailSender javaMailSender;
    private final MailService mailService;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final CvService cvService;
    public UserServiceImpl(UserRepo userRepo, ModelMapper mapper, MapperService mapperService, CountryRepo countryRepo, CityRepo cityRepo, LanguageRepo languageRepo, TechnologyRepo technologyRepo, JavaMailSender javaMailSender, MailService mailService, TokenRepo tokenRepo, PasswordEncoder passwordEncoder, CvService cvService) {
        this.userRepo = userRepo;
        this.mapper = mapper;
        this.mapperService = mapperService;
        this.countryRepo = countryRepo;
        this.cityRepo = cityRepo;
        this.languageRepo = languageRepo;
        this.technologyRepo = technologyRepo;
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
        this.tokenRepo = tokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.cvService = cvService;
    }

    @Value("${domain}")
    private String domain;
    @Override
    public ResponseModel<List<UserResponse>> getAllUsers() {
        List<User> users = userRepo.findAll();
        if(users.isEmpty())
            return ResponseModel.<List<UserResponse>>builder()
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .model(new ArrayList<>())
                    .build();
        	List<UserResponse> userResponses = users.stream().map(u->{
            UserResponse response = mapper.map(u,UserResponse.class);
            CvResponse cvResponse = mapper.map(u.getCv(),CvResponse.class);
            Set<LanguageResponse> languageResponses = u.getLanguages().stream()
                    .map(l->mapper.map(l,LanguageResponse.class)).collect(Collectors.toSet());
            Set<TechnologyResponse> technologyResponses = u.getTechnology().stream()
                    .map(t->mapper.map(t,TechnologyResponse.class)).collect(Collectors.toSet());
            AddressResponse addressResponse = mapper.map(u.getAddress(), AddressResponse.class);
            CountryResponse countryResponse = mapper.map(u.getAddress().getCountry(),CountryResponse.class);
            CityResponse cityResponse = mapper.map(u.getAddress().getCity(),CityResponse.class);
            addressResponse.setCountry(countryResponse);
            addressResponse.setCity(cityResponse);
            response.setCv(cvResponse);
            response.setLanguages(languageResponses);
            response.setTechnology(technologyResponses);
            response.setAddress(addressResponse);
            return response;
        }).collect(Collectors.toList());
        return ResponseModel.<List<UserResponse>>builder()
                .response(Response.getSuccess())
                .model(userResponses)
                .build();
    }




    @SneakyThrows
    @Override
    public void updateUser(UserDto userDto, Long id){
        User user = userRepo.findById(id)
                .orElseThrow(()->new UserException(Status.USER_NOT_FOUND));
        User updatedUser = mapperService.updateObject(userDto,user);
        updatedUser.setId(user.getId());
        Country country = user.getAddress().getCountry();
        City city = user.getAddress().getCity();
        if(userDto.getAddress()!=null) {
            if (userDto.getAddress().getCountry() != null)
                country = mapper.map(userDto.getAddress().getCountry(), Country.class);
            if (userDto.getAddress().getCity() != null)
                city = mapper.map(userDto.getAddress().getCity(), City.class);
            if (!countryRepo.existsById(country.getId()))
                throw new CountryException(Status.COUNTRY_NOT_FOUND);
            if (!cityRepo.existsById(city.getId()))
                throw new CityException(Status.CITY_NOT_FOUND);
            if (cityRepo.getById(city.getId()).getCountry().getId() != country.getId())
                throw new CityException(Status.CITY_NOT_EXIST_IN_COUNTRY);
            Address address = mapperService.updateObject(userDto.getAddress(),user.getAddress());
            address.setCountry(country);
            address.setCity(city);
            address.setId(user.getAddress().getId());
            updatedUser.setAddress(address);
        }
        if(!userDto.getLanguages().isEmpty()) {
            Set<Language> languages = userDto.getLanguages().stream()
                    .map(l -> {
                        if (!languageRepo.existsById(l.getId()))
                            try {
                                throw new LanguageException(Status.LANGUAGE_NOT_FOUND);
                            } catch (LanguageException e) {
                                throw new RuntimeException(e);
                            }
                        return mapper.map(l, Language.class);
                    }).collect(Collectors.toSet());
            updatedUser.setLanguages(languages);
        }
        if(!userDto.getTechnology().isEmpty()){
            Set<Technology> technologies = userDto.getTechnology().stream()
                    .map(t->{
                        if(!technologyRepo.existsById(t.getId()))
                            try {
                                throw new TechnologyException(Status.TECHNOLOGY_NOT_FOUND);
                            } catch (TechnologyException e) {
                                throw new RuntimeException(e);
                            }
                        return mapper.map(t, Technology.class);
                    }).collect(Collectors.toSet());
            updatedUser.setTechnology(technologies);
        }
        if(!user.getUsername().equalsIgnoreCase(updatedUser.getUsername()) &
                userRepo.findByUsernameAndEnabled(updatedUser.getUsername(), true).isPresent()) {
            throw new UserException(Status.USERNAME_EXISTS);
        }
        if(!user.getEmail().equalsIgnoreCase(updatedUser.getEmail()) &
                userRepo.findByEmailAndEnabled(updatedUser.getEmail(), true).isPresent()) {
            throw new UserException(Status.EMAIL_EXISTS);
        }
        if(!user.getEmail().equalsIgnoreCase(updatedUser.getEmail())){
            Token token = new Token(updatedUser, updatedUser.getEmail());
            Token savedToken = tokenRepo.save(token);
            sendConfirmMail(savedToken);
        }
        updatedUser.setUpdatedDate(new Date());
        if(!user.getPassword().equalsIgnoreCase(updatedUser.getPassword()))
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userRepo.save(updatedUser);
    }

    @Override
    public ResponseModel<UserResponse> getUserById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(!userOptional.isPresent())
            return ResponseModel.<UserResponse>builder()
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        UserResponse userResponse = mapper.map(userOptional.get(),UserResponse.class);
        CountryResponse countryResponse = mapper.map(userOptional.get().getAddress().getCountry(),CountryResponse.class);
        CityResponse cityResponse = mapper.map(userOptional.get().getAddress().getCity(),CityResponse.class);
        AddressResponse addressResponse = mapper.map(userOptional.get().getAddress(),AddressResponse.class);
        addressResponse.setCountry(countryResponse);
        addressResponse.setCity(cityResponse);
        CvResponse cvResponse = mapper.map(userOptional.get().getCv(), CvResponse.class);
        Set<LanguageResponse> languageResponses = userOptional.get().getLanguages().stream()
                .map(l->mapper.map(l,LanguageResponse.class)).collect(Collectors.toSet());
        Set<TechnologyResponse> technologyResponses = userOptional.get().getTechnology().stream()
                .map(t->mapper.map(t, TechnologyResponse.class)).collect(Collectors.toSet());
        userResponse.setAddress(addressResponse);
        userResponse.setCv(cvResponse);
        userResponse.setLanguages(languageResponses);
        userResponse.setTechnology(technologyResponses);
        return ResponseModel.<UserResponse>builder().response(Response.getSuccess()).model(userResponse).build();
    }

    @SneakyThrows
    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(()->new UserException(Status.USER_NOT_FOUND));
        user.getLanguages().forEach(l->user.getLanguages().remove(l));
        user.getTechnology().forEach(t->user.getTechnology().remove(t));
        user.getNotifications().forEach(n->user.getNotifications().remove(n));
        user.getTasks().forEach(t->user.getTasks().remove(t));
        cvService.deleteCvFromFolder(user.getCv().getPath());
        userRepo.save(user);
        userRepo.delete(user);
    }

    @SneakyThrows
    public void deleteDisableUserByEmailOrUsername(String username, String email, boolean enabled){
        User user = userRepo.findDisableUser(username,email,enabled).orElse(new User());
        if(user.getId()==null)
            return;
        user.getLanguages().forEach(l->user.getLanguages().remove(l));
        user.getTechnology().forEach(t->user.getTechnology().remove(t));
        user.getNotifications().forEach(n->user.getNotifications().remove(n));
        user.getTasks().forEach(t->user.getTasks().remove(t));
        cvService.deleteCvFromFolder(user.getCv().getPath());
        userRepo.save(user);
        userRepo.delete(user);
    }

    @Override
    public ResponseModel<UserResponse> getLoggedInUser(Authentication auth) {
        User user = (User)auth.getPrincipal();
        if(user==null)
            return ResponseModel.<UserResponse>builder()
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        UserResponse userResponse = mapper.map(user,UserResponse.class);
        return ResponseModel.<UserResponse>builder()
                .model(userResponse)
                .response(Response.getSuccess())
                .build();
    }

    @SneakyThrows
    @Override
    public void changeMail(String token) {
        Token t = tokenRepo.findByToken(token)
                .orElseThrow(()->new TokenException(Status.TOKEN_NOT_FOUND));
        if(t.getExpiredDate().before(new Date()))
            throw new TokenException(Status.TOKEN_HAS_EXPIRED);
        userRepo.changeMail(t.getUser().getId(), t.getEmail());
        tokenRepo.deleteById(t.getId());
    }

    @SneakyThrows
    public void sendConfirmMail(Token token){
        String from = "NSP";
        String to = token.getEmail();
        String subject = "Confirm your email address";
        String link =  domain + "user/confirm/"+token.getToken();
        String body = "<p>Hi, "+token.getUser().getName()+"<br><br>Please click <a href='"+link+"'>here</a>" +
                " and confirm your new email address";
        mailService.sendMail(from,to,subject,body);
    }


}
