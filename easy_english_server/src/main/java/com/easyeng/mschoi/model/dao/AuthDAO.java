package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easyeng.mschoi.model.dto.Member;

public interface AuthDAO extends JpaRepository<Member, Integer>{
	
	// 1. JPA 메소드 이름으로 쿼리 생성
	Member findByMemberId(String memberId);
}
