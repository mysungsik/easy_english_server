package com.easyeng.mschoi.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

	// 엑셀 파일 업로드
	int uploadFile(MultipartFile file, String uploadPath, String fileName);
	
	// 엑셀 파일 읽고 데이터베이스 저장
	int readAndSaveFile(File savedFile);
	
	// 네이버 사전 검색
	String searcNaverAPI(String word);
	
}
