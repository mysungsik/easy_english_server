package com.easyeng.mschoi.controller;

import com.easyeng.mschoi.model.dto.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyeng.mschoi.service.SignupService;

@RestController
@RequestMapping("/api/signup")
public class SignupController {
	
	@Autowired
	private SignupService service;
	
	@PostMapping("")
	public int signup (@RequestBody Member member) {
		
		int result = service.signup(member);
		return result;
	}
}
