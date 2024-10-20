package com.easyeng.mschoi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.easyeng.mschoi.model.dto.WordData;
import com.easyeng.mschoi.service.LearnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/learn")
public class LearnController {
	
	private final LearnService service;

	@GetMapping("/getCurrentWordForMemeber")
	public WordData getCurrentWordForMemeber(@RequestParam("memberNo") int memberNo,
											@RequestParam(name = "currentWordId", defaultValue = "0") int currentWordId ) {
		WordData result = service.getCurrentWordForMemeber(memberNo, currentWordId);

		return result;
	}
}
