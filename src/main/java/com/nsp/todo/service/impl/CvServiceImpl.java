package com.nsp.todo.service.impl;

import com.nsp.todo.enums.Status;
import com.nsp.todo.exception.CvException;
import com.nsp.todo.model.Cv;
import com.nsp.todo.model.User;
import com.nsp.todo.model.response.CvResponse;
import com.nsp.todo.model.response.Response;
import com.nsp.todo.model.response.ResponseModel;
import com.nsp.todo.repository.CvRepo;
import com.nsp.todo.repository.UserRepo;
import com.nsp.todo.service.CvService;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CvServiceImpl implements CvService {

    @Value("${rootPath}")
    private String rootPath;
    @Value("${cvPath}")
    private String resumePath;
    @Value("${domain}")
    private String domain;


    private final CvRepo cvRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;

    public CvServiceImpl(CvRepo cvRepo, ModelMapper modelMapper, UserRepo userRepo) {
        this.cvRepo = cvRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    @SneakyThrows
    @Override
    public String uploadCv(User user, MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            throw new CvException(Status.CV_IS_EMPTY);
        byte[] cv = IOUtils.toByteArray(multipartFile.getInputStream());
        String cvName = multipartFile.getOriginalFilename();
        String cvPath = rootPath + "cv/" + user.getId() + cvName;
        File file = new File(cvPath);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(cv);
        fos.flush();
        fos.close();
        Cv c = Cv.builder().path(domain+resumePath+user.getId()+cvName).user(user).build();
        Cv savedCv = cvRepo.save(c);
        return savedCv.getPath();
    }



    @Override
    public void deleteCvFromFolder(String path) {
        String originalPath = "src/main/resources/" + path.substring(domain.length());
        File file = new File(originalPath);
        if(file.exists())
            file.delete();
    }

    @Override
    public ResponseModel<List<CvResponse>> getAllCv() {
        List<Cv> cvs = cvRepo.findAll();
        if (cvs.isEmpty())
            return ResponseModel.<List<CvResponse>>builder()
                    .model(new ArrayList<>())
                    .response(Response.getNotFound(Status.CV_NOT_FOUND))
                    .build();
        List<CvResponse> cvResponses = cvs.stream()
                .map(c->modelMapper.map(c, CvResponse.class))
                .collect(Collectors.toList());
        return ResponseModel.<List<CvResponse>>builder()
                .model(cvResponses)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<CvResponse> getCvById(Long id) {
        Optional<Cv> cvOptional = cvRepo.findById(id);
        if(!cvOptional.isPresent())
            return ResponseModel.<CvResponse>builder()
                    .response(Response.getNotFound(Status.CV_NOT_FOUND))
                    .build();
        CvResponse cvResponse = modelMapper.map(cvOptional.get(), CvResponse.class);
        return ResponseModel.<CvResponse>builder()
                .model(cvResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Override
    public ResponseModel<CvResponse> getCvByUserId(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if(!userOptional.isPresent())
            return ResponseModel.<CvResponse>builder()
                    .response(Response.getNotFound(Status.USER_NOT_FOUND))
                    .build();
        CvResponse cvResponse = modelMapper.map(userOptional.get().getCv(),CvResponse.class);
        return ResponseModel.<CvResponse>builder()
                .model(cvResponse)
                .response(Response.getSuccess())
                .build();
    }

    @Transactional
    @SneakyThrows
    @Override
    public void updateCv(MultipartFile multipartFile, Long id) {
        Cv cv = cvRepo.findById(id)
                .orElseThrow(()->new CvException(Status.CV_NOT_FOUND));
        deleteCvFromFolder(cv.getPath());
        String path = updateCvPath(cv,multipartFile);
        cv.setPath(path);
        cv.setUpdatedDate(new Date());
        cvRepo.save(cv);
    }

    @SneakyThrows
    @Override
    public String updateCvPath(Cv cv, MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            throw new CvException(Status.CV_IS_EMPTY);
        deleteCvFromFolder(cv.getPath());
        byte[] updateCv = IOUtils.toByteArray(multipartFile.getInputStream());
        String cvName = multipartFile.getOriginalFilename();
        String cvPath = rootPath + "/upload/cv/" + cv.getUser().getId() + cvName;
        File file = new File(cvPath);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(updateCv);
        fos.flush();
        fos.close();
        return domain + resumePath + cv.getUser().getId() + multipartFile.getOriginalFilename();
    }
}
