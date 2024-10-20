package com.easyeng.mschoi.service;

import com.easyeng.mschoi.model.dto.WordData;

public interface LearnService {

	// 현재 문제에 대한 정보 가져오기
	WordData getCurrentWordForMemeber(int memberNo, int wordId);

	// GEMINI API 사용하여 예시문장 생성
	WordData createExampleSetence(WordData word);
}
