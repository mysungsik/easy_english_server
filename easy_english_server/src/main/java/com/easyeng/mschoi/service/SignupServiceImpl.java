package com.easyeng.mschoi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyeng.mschoi.model.dao.SignupDAO;
import com.easyeng.mschoi.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 자동 의존성 주입
public class SignupServiceImpl  implements SignupService{
	
	private final SignupDAO dao;
	
	@Override
	@Transactional
	public int signup(Member member) {
		
	    try {
	        Member savedMember = dao.save(member);
	        return savedMember.getMemberNo(); // 저장된 객체의 ID 반환
	    } catch (Exception e) {
	        System.out.println("[Error] SIGN UP");
	        return 0; // 실패 시 0 반환
	    }
	}
}
