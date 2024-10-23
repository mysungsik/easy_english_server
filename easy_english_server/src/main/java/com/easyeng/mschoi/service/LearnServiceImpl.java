package com.easyeng.mschoi.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.easyeng.mschoi.config.webclients.WebClientInterface;
import com.easyeng.mschoi.model.dao.MemberDAO;
import com.easyeng.mschoi.model.dao.RepeatNoteDAO;
import com.easyeng.mschoi.model.dao.WordDataDAO;
import com.easyeng.mschoi.model.dto.Member;
import com.easyeng.mschoi.model.dto.RepeatNote;
import com.easyeng.mschoi.model.dto.WordData;
import com.easyeng.mschoi.model.dto.gemeni.GeminiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearnServiceImpl implements LearnService {
	
	private final MemberDAO memberDAO;
	private final WordDataDAO wordDAO;
	private final RepeatNoteDAO repeatNoteDAO;
	private final ObjectMapper objectMapper;
	
	@Autowired
	@Qualifier("geminiWebClient")
	private WebClientInterface geminiWebClient; 

	/** 현재 멤버에 해당하는 오늘의 문제 가져오기
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public WordData getCurrentWordForMemeber(int memberNo,int currentWordId) {
		
		Member member = memberDAO.findById(memberNo).orElse(null);
		WordData word = null;
		
		if (member != null) {
			
			// 하루 최고 문제풀이 20문제
			if (member.getLearnCnt() >= 20) {
				return word;
			}else {
				// currentWordId == 0 : 현재 레벨의 문제 GET
				// currentWordId != 0 : 클라이언트가 정답 제출, 다음 문제를 위해 WordId + 1 업데이트
				if (currentWordId != 0) {
					memberDAO.updateMemberWordId(memberNo, currentWordId + 1);
					memberDAO.updateMemberLearnCnt(memberNo, member.getLearnCnt() + 1);
					word = wordDAO.findById(currentWordId + 1).orElse(null);
				} else {
					word = memberDAO.findCurrentWordDataByMemberId(memberNo);				
				}
				
				if(word != null) {
					// word 에 대해 예시문장이 없다면 생성
					if (word.getExampleSentence().equals("") || word.getExampleSentence() == null
							|| word.getExampleMean().equals("") || word.getExampleMean() == null) {
						
						// word에 대한 문장 및 문장 뜻 생성
						word = this.createExampleSetence(word);
						wordDAO.save(word);
					}
				}
			}
		}
		
		return word;
	}

	/** GEMINI API 사용하여 예시문장 생성
	 *
	 */
	@Override
	public WordData createExampleSetence(WordData word) {
		WebClient request = geminiWebClient.createWebClient();
		
		Map<String, Object> requestBody = new HashMap<>();
		
		String wordSpell = word.getWordSpell();
		String wordMean = word.getWordMean();
		
		requestBody.put("contents", 
					List.of(Map.of("parts", 
								List.of(Map.of("text", 
										String.format(
											"""
											'%s' 이 글자그대로 포함된 '의미있는' 영어 문장 하나 알려줘
											'%s' 의 뜻은 '%s' 야
											또한 영어문장을 한국어로 번역해서 결과에 저장해줘
											영어문장의 spell은 반드시 60 spell 이상 이 되도록 해줘,
											결과는 아래의 형식을 따라
											{
											    "exampleSentence" : "영어문장",
											    "exampleMean" : "영어문장의 한국어 해석"
											}
											다른 설명은 필요 없이 해당 형식으로만 응답을 줘
												""", 
											wordSpell, wordSpell, wordMean))
										)
								)
							)
					);
		
		GeminiResponse response = request.post()
			.uri("")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(GeminiResponse.class)
			.block();
		
		String responseText = response.getCandidates().get(0).getContent().getParts().get(0).getText();
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> result = objectMapper.readValue(responseText, Map.class);
			word.setExampleSentence(result.get("exampleSentence"));
			word.setExampleMean(result.get("exampleMean"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.println("[ERROR] : 문장 생성 실패");
		}
		
		return word;
	}

	/** 단어장에 단어 저장 함수
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RepeatNote saveToRepatNote(RepeatNote repeatNote) {
		RepeatNote result = null;
		
		try {
			result = repeatNoteDAO.save(repeatNote);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR] : 단어 저장 실패");
		}
		
		return result;
	}

	/** 단어장 가져오기
	 *
	 */
	@Override
	public List<WordData> getRepeatNoteByMemberNo(int memberNo) {
		List<WordData> result = repeatNoteDAO.findWordDataByMemberNo(memberNo);
		return result;
	}

	/** 복습용 20개의 데이터 가져오기
	 *
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public WordData getRandomWordForReviewByMember(int memberNo) {		
		Member member = memberDAO.findById(memberNo).orElse(null);
		WordData result = null;
		
		if (member != null) {
			// 하루 최고 문제풀이 20문제
			if (member.getReviewCnt() >= 20) {
				return result;
			}else {
				// 유저가 최근까지 공부한 wordId
				int limitWordId = member.getWordData().getWordId();

				// 최근 값 이내로 하나의 랜덤 wordData GET
				result = wordDAO.getRandomWordForReviewByMember(limitWordId);
				
				// 복습량 업데이트
				if (result != null) {
					memberDAO.updateMemberReviewCnt(memberNo, member.getReviewCnt() + 1);
				}
			}
		}
		return result;
	}

}
