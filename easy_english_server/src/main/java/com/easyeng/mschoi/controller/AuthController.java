package com.easyeng.mschoi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyeng.mschoi.model.dto.Member;
import com.easyeng.mschoi.service.AuthService;
import com.easyeng.mschoi.utils.JwtUtil;

@RestController
@RequestMapping("/api/login")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("")
	public ResponseEntity<String> login(@RequestBody Member member) {
		try {
			// 1. SpringSecurity Filter 에 의해 사용자 인증 => 인증정보 생성
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getMemberPw()));
			
			// 2. Security Context 에 인증 정보를 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			Member memberInfo = service.getMemberInfoById(member.getMemberId());
			
			// 3. JWT 토큰 생성
			Map<String, Object> claims = new HashMap<>();
			claims.put("memberId", member.getMemberId());
			claims.put("memberNo", memberInfo.getMemberNo());
			claims.put("memberNickname", memberInfo.getMemberNickname());
			claims.put("memberEmail", memberInfo.getMemberEmail());
			claims.put("memberAuth", memberInfo.getMemberAuth());
			String jwt = jwtUtil.createJWT(claims, member.getMemberId());
			
			// 4. JWT 응답
			return ResponseEntity.ok(jwt);
		
		} catch (Exception e) {
			// 입력정보 잘못되었을경우 Login 해당 메시지가 전달
			return ResponseEntity.badRequest().body("Login failed: " + e);
		}
	}
}
