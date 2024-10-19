package com.easyeng.mschoi.controller;

import java.io.File;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easyeng.mschoi.service.AdminService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {	
	
	@Value("${project.uploadpath}")
	private String uploadPath;
	
	private final AdminService service;
	
	@PostMapping("/uploadExcel")
	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
		
		String fileExp;
		String fileName;
		
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정상적인 파일이 아닙니다.");			
		}
		
		// 확장자 추출
		fileExp = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		if (!fileExp.equals("xlsx")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 파일 확장자입니다.");	
		}
		
		try {
			// 파일 업로드 서비스
			fileName = file.getOriginalFilename();
			int uploadResult = service.uploadFile(file, uploadPath, fileName);
			if(uploadResult == 0) {
				throw new FileUploadException();
			}
			
			// 파일 읽고 데이터베이스 저장 서비스
			File savedFile = new File(uploadPath + fileName);
			int saveResult = service.readAndSaveFile(savedFile);
			
			if (saveResult == 0) {
				throw new IOException();
			}
		} catch (FileUploadException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드에 실패하였습니다.");		
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 읽기에 실패하였습니다.");		
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드에 실패하였습니다.");		
		}
		
		return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다.");
	}
}
