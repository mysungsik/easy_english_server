package com.easyeng.mschoi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
	
	@GetMapping("/test")
	public String test() {
		return "hello";
	}
}
