package com.easyeng.mschoi.service;

import com.easyeng.mschoi.model.dto.WordData;

public interface LearnService {

	WordData getCurrentWordForMemeber(int memberNo);

	// GEMINI API 사용하여 예시문장 생성
	WordData createExampleSetence(WordData word);
}
