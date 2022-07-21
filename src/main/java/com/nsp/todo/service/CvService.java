package com.nsp.todo.service;

import com.nsp.todo.model.Cv;
import com.nsp.todo.model.User;
import com.nsp.todo.model.response.CvResponse;
import com.nsp.todo.model.response.ResponseModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CvService {
   String uploadCv(User user, MultipartFile multipartFile);

   void deleteCvFromFolder(String path);

    ResponseModel<List<CvResponse>> getAllCv();

    ResponseModel<CvResponse> getCvById(Long id);

    ResponseModel<CvResponse> getCvByUserId(Long userId);

    void updateCv(MultipartFile multipartFile, Long id);

    String updateCvPath(Cv cv,MultipartFile multipartFile);
}
