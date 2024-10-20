package com.easyeng.mschoi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
	public WordData getCurrentWordForMemeber(@RequestParam("memberNo") int memberNo,
											@RequestParam(name = "currentWordId", defaultValue = "0") int currentWordId ) {
		WordData result = service.getCurrentWordForMemeber(memberNo, currentWordId);

		return result;
	}
	
	// 단어장에 저장
	@PostMapping("/saveToRepatNote")
	public ResponseEntity<String> saveToRepatNote(@RequestBody RepeatNote repeatNote){
		
		RepeatNote result = service.saveToRepatNote(repeatNote);
		if (result != null) {
			return ResponseEntity.ok("성공적으로 저장하였습니다.");			
		}else {
			return ResponseEntity.status(400).body("단어 저장에 실패하였습니다.");
		}
	}
	
	// 단어장 확인
	@GetMapping("/getRepeatNoteByMemberNo")
	public ResponseEntity<List<WordData>> getRepeatNoteByMemberNo(@RequestParam("memberNo") int memberNo){
		
		List<WordData> result = service.getRepeatNoteByMemberNo(memberNo);
		
		System.out.println(memberNo);
		System.out.println(result);
		if (result != null) {
			return ResponseEntity.ok(result);			
		}else {
			return ResponseEntity.status(400).body(result);
		}
	}
}
