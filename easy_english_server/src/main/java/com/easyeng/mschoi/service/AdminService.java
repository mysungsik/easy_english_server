package com.easyeng.mschoi.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

	// 엑셀 파일 업로드
	int uploadFile(MultipartFile file, String uploadPath, String fileName);
	
	// 엑셀 파일 읽고 데이터베이스 저장
	int readAndSaveFile(File savedFile);
	
	// 네이버 백과사전 검색 (백과사전이라 영어사전 불가능)
	String searcNaverAPI(String word);
	
	// Google Cloud Translation API ( 부분유료, 번역 )
	String translateWithGCP(String word);
	
}
