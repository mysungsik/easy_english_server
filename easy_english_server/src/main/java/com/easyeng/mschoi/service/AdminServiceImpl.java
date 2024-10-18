package com.easyeng.mschoi.service;

import java.io.File;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.easyeng.mschoi.model.dao.AdminDAO;
import com.easyeng.mschoi.model.dto.WordData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final AdminDAO dao;

	// 파일 업로드 서비스
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

	// 파일 읽고 저장하는 서비스
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
				
				// 기존 데이터가 있다면, 업데이트하도록 find && Optional 객체이므로 orElse 사용가능
				WordData wordData = dao.findByWordSpell(wordSpellData).orElse(new WordData());
				wordData.setWordSpell(wordSpellData);
				int wordLevel = (!wordLevelData.equals("")) ? Integer.parseInt(wordLevelData) : 1;
				wordData.setWordLevel(wordLevel);
				wordData.setWordMean(wordMeanData);
				wordData.setExampleSentence(exampleSentenceData);
				wordData.setExampleMean(exampleMeanData);
				
				dao.save(wordData);
			}
			
		}catch (Exception e) {
			return 0;
		}

		return 1;

	}

}
