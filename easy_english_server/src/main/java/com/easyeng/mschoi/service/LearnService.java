package com.easyeng.mschoi.service;

import java.util.List;

import com.easyeng.mschoi.model.dto.RepeatNote;
import com.easyeng.mschoi.model.dto.WordData;

public interface LearnService {

	// 현재 문제에 대한 정보 가져오기
	WordData getCurrentWordForMemeber(int memberNo, int wordId);

	// GEMINI API 사용하여 예시문장 생성
	WordData createExampleSetence(WordData word);

	// 단어장 저장
	RepeatNote saveToRepatNote(RepeatNote repeatNote);

	// 단어장 가져오기
	List<WordData> getRepeatNoteByMemberNo(int memberNo);

	// 복습용 데이터 가져오기
	WordData getRandomWordForReviewByMember(int memberNo);
}
