package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyeng.mschoi.model.dto.Member;
import com.easyeng.mschoi.model.dto.WordData;

public interface LearnDAO extends JpaRepository<Member, Integer>{
	// JPA 에서는 "객체 자체(클래스이름)" 를 SELECT 문에 쓸 수 있으며, 해당 결과를 받을 수 있다.
	@Query("SELECT m.wordData FROM Member m WHERE m.memberNo = :memberNo")
	WordData findCurrentWordDataByMemberId(@Param("memberNo") int memberNo);
}
