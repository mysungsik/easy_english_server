package com.easyeng.mschoi.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

	int uploadFile(MultipartFile file, String uploadPath, String fileName);

	int readAndSaveFile(File savedFile);
}
