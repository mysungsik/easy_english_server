package com.easyeng.mschoi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyeng.mschoi.model.dto.RepeatNote;
import com.easyeng.mschoi.model.dto.WordData;
import com.easyeng.mschoi.service.LearnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learn")
public class LearnController {
	
	private final LearnService service;

	// GET 현재 공부할 단어
	@GetMapping("/getCurrentWordForMemeber")
	public ResponseEntity<Map<String, Object>> getCurrentWordForMemeber(@RequestParam("memberNo") int memberNo,
											@RequestParam(name = "currentWordId", defaultValue = "0") int currentWordId ) {
		WordData wordData = service.getCurrentWordForMemeber(memberNo, currentWordId);
		
		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", wordData);			
		if (wordData != null) {
			result.put("message", "새 단어를 정상적으로 데이터를 가져왔습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "새 단어를 가져오는데 실패하였습니다.");
			return ResponseEntity.status(400).body(result);
		}

	}
	
	// 단어장에 저장
	@PostMapping("/saveToRepatNote")
	public ResponseEntity<Map<String, Object>> saveToRepatNote(@RequestBody RepeatNote repeatNote){
		
		RepeatNote repeat = service.saveToRepatNote(repeatNote);

		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", repeat);			
		if (repeat != null) {
			result.put("message", "단어장에 성공적으로 저장하였습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "단어장 저장에 실패하였습니다.");
			return ResponseEntity.status(400).body(result);
		}
	}
	
	// 개별 단어장 확인
	@GetMapping("/checkWordFromRepeatNote")
	public ResponseEntity<Map<String, Object>> saveToRepatNote(@RequestParam("memberNo") int memberNo,
																@RequestParam("wordId") int wordId){
		
		Integer repeat = service.checkWordFromRepeatNote(memberNo, wordId);

		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", repeat);			
		if (repeat >= 0) {
			result.put("message", "단어장에서 개별 단어를 가져오는데 성공했습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "단어장에서 개별 단어를 가져오는데 실패했습니다.");
			return ResponseEntity.status(400).body(result);
		}
	}
	
	// 모든 단어장 확인
	@GetMapping("/getAllWordsFromRepeatNote")
	public ResponseEntity<Map<String, Object>> getAllWordsFromRepeatNote(@RequestParam("memberNo") int memberNo){
		
		List<WordData> repeatList = service.getAllWordsFromRepeatNote(memberNo);

		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", repeatList);			
		if (repeatList.size() > 0) {
			result.put("message", "단어장에 성공적으로 가져왔습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "단어장 저장에 실패하였습니다.");
			return ResponseEntity.status(400).body(result);
		}
	}
	
	// 개별 단어장 삭제
	@DeleteMapping("/deleteWordFromRepatNote")
	public ResponseEntity<Map<String, Object>> deleteWordFromRepatNote(@RequestParam("memberNo") int memberNo,
																		@RequestParam("wordId") int wordId){
		
		Integer repeat = service.deleteWordFromRepatNote(memberNo, wordId);

		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", repeat);			
		if (repeat >= 0) {
			result.put("message", "단어장에서 삭제에 성공했습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "단어장에서 삭제에 실패하였습니다.");
			return ResponseEntity.status(400).body(result);
		}
	}

	
	
	// 복습용 랜덤 20개의 데이터 가져오기
	@GetMapping("/getRandomWordForReviewByMember")
	public ResponseEntity<Map<String, Object>> getRandomWordForReviewByMember(@RequestParam("memberNo") int memberNo){
		
		
		WordData reviewWord = service.getRandomWordForReviewByMember(memberNo);

		Map<String, Object> result = new HashMap<>();
		
		// 데이터 반환
		result.put("data", reviewWord);			
		if (reviewWord != null) {
			result.put("message", "복습단어를 가져오는데 성공했습니다.");
			return ResponseEntity.ok(result);
		}else {
			result.put("message", "복습단어를 가져오는데 실패하였습니다.");
			return ResponseEntity.status(400).body(result);
		}
	}
	
}
