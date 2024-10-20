package com.easyeng.mschoi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.easyeng.mschoi.model.dao.MemberDAO;
import com.easyeng.mschoi.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private final MemberDAO dao;

	// Spring Security 에서 사용하는 인증 로직
	// 검색된 사용자 정보를 기반으로 비밀번호 검증은 Spring Security의 AuthenticationProvider가 내부적으로 수행
	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Member memberInfo = null;
		
		try {
			System.out.println(memberId);
			memberInfo = dao.findByMemberId(memberId);
			
		} catch (Exception e) {
			System.out.println("[Error] findByMemberid");
		}
		
		return memberInfo;
	}
}
