package com.easyeng.mschoi.service;

import org.springframework.stereotype.Service;

import com.easyeng.mschoi.model.dao.AuthDAO;
import com.easyeng.mschoi.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthDAO dao;

	@Override
	public Member getMemberInfoById(String memberId) {
		Member memberInfo = null;

		try {
			memberInfo = dao.findByMemberId(memberId);
		} catch (Exception e) {
			System.out.println(e);
		}

		return memberInfo;
	}
}
