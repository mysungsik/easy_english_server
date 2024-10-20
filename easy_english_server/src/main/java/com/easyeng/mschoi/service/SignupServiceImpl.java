package com.easyeng.mschoi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyeng.mschoi.model.dao.MemberDAO;
import com.easyeng.mschoi.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 자동 의존성 주입
public class SignupServiceImpl  implements SignupService{
	
	private final MemberDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	@Transactional
	public int signup(Member member) {
		
	    try {
	    	member.setMemberPw(bCryptPasswordEncoder.encode(member.getMemberPw()));
	        Member savedMember = dao.save(member);
	        return savedMember.getMemberNo(); // 저장된 객체의 ID 반환
	    } catch (Exception e) {
	        System.out.println("[Error] SIGN UP : " + e);
	        return 0; // 실패 시 0 반환
	    }
	}
}
