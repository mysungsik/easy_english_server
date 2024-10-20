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
import com.easyeng.mschoi.model.dao.WordDataDAO;
import com.easyeng.mschoi.model.dto.Member;
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
	private final ObjectMapper objectMapper;
	
	@Autowired
	@Qualifier("geminiWebClient")
	private WebClientInterface geminiWebClient; 

	@Override
	@Transactional(rollbackFor = Exception.class)
	public WordData getCurrentWordForMemeber(int memberNo) {
		Member member = memberDAO.findById(memberNo).orElse(null);
		WordData word = null;
		
		if (member != null) {
			word = member.getWordData();
			memberDAO.updateMemberWordId(memberNo, word.getWordId() + 1);
			
			// word 에 대해 예시문장이 없다면 생성
			if (word.getExampleSentence().equals("") || word.getExampleSentence() == null
					|| word.getExampleMean().equals("") || word.getExampleMean() == null) {
				
				// 글자에 대한 문장 및 문장 뜻 생성
				word = this.createExampleSetence(word);
				
				System.out.println("생성한 문장 저장 :" + word.getExampleSentence());
				
				// 글자 데이터 DB 업데이트
				wordDAO.save(word);
			}
		}
		
		return word;
	}

	// GEMINI API 사용하여 예시문장 생성
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
}
