package com.easyeng.mschoi.service;

import java.io.File;
import com.google.cloud.translate.*;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.easyeng.mschoi.config.webclients.WebClientInterface;
import com.easyeng.mschoi.model.dao.WordDataDAO;
import com.easyeng.mschoi.model.dto.WordData;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final WordDataDAO dao;
	
	private Translate translate = TranslateOptions.getDefaultInstance().getService();
	
	@Autowired
	@Qualifier("naverWebClient")
	private WebClientInterface naverWebClient;
	
	// 엑셀 파일 업로드 서비스
	@Override
	public int uploadFile(MultipartFile file, String uploadPath, String fileName) {
		try {
			File destPath = new File(uploadPath + fileName);
			file.transferTo(destPath);

			return 1;
		} catch (Exception e) {
			System.out.println("파일 업로드에 실패하였습니다.");
			return 0;
		}
	}

	// 엑셀 파일 읽고 데이터베이스 저장 서비스
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int readAndSaveFile(File file){
		try(XSSFWorkbook workbook = new XSSFWorkbook(file)){
			XSSFSheet workSheet = workbook.getSheetAt(0);
			for (int i = 1; i< workSheet.getPhysicalNumberOfRows(); i++) {

				DataFormatter formatter = new DataFormatter();
				XSSFRow row = workSheet.getRow(i);

				String wordSpellData = formatter.formatCellValue(row.getCell(0));
				String wordLevelData = formatter.formatCellValue(row.getCell(1));
				String wordMeanData = formatter.formatCellValue(row.getCell(2));
				String exampleSentenceData = formatter.formatCellValue(row.getCell(3));
				String exampleMeanData = formatter.formatCellValue(row.getCell(4));
				
				// 기존 데이터가 있다면, 업데이트하도록 find
				// Optional 객체이므로 orElse 사용가능
				WordData wordData = dao.findByWordSpell(wordSpellData).orElse(new WordData());
				
				wordData.setWordSpell(wordSpellData);
				
				// 단어 레벨이 비어있다면 1, 아니면 파싱
				int wordLevel = (!wordLevelData.equals("")) ? Integer.parseInt(wordLevelData) : 1;
				wordData.setWordLevel(wordLevel);
				
				// 단어 뜻이 비었다면 네이버 백과사전 검색
				String wordMean = (wordMeanData.equals("") ? this.translateWithGCP(wordSpellData) : wordMeanData);
				wordData.setWordMean(wordMean);
				
				wordData.setExampleSentence(exampleSentenceData);
				wordData.setExampleMean(exampleMeanData);
				
				dao.save(wordData);
			}
			
		}catch (Exception e) {
			return 0;
		}

		return 1;
	}

	// 네이버 사전 검색 서비스
	@Override
	public String searcNaverAPI(String word) {
	    
	    try {
	        String searchResultBlock = naverWebClient.createWebClient()
	                                   .get()
	                                   .uri(uriBuilder -> uriBuilder.path("").queryParam("query",word).build())
	                                   .retrieve()
	                                   .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
	                                             clientResponse -> Mono.error(new RuntimeException("API 요청 오류")))
	                                   .bodyToMono(String.class)
	                                   .block();
	        return searchResultBlock;
	    } catch (Exception e) {
	        System.out.println("API 요청 중 오류 발생: " + e.getMessage());
	        return null; // 오류 처리
	    }
	}

	// GCP 의 Translation API 사용하여 번역
	@Override
	public String translateWithGCP(String word) {
		try {
			Translation translation = translate.translate(
					word, 
					Translate.TranslateOption.sourceLanguage("en"),
					Translate.TranslateOption.targetLanguage("ko")
				);
		    String translatedText = translation.getTranslatedText();
		    return translatedText;

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		return null;
	}

}
