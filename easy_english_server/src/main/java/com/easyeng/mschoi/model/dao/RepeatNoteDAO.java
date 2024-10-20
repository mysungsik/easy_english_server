package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyeng.mschoi.model.dto.RepeatNote;
import com.easyeng.mschoi.model.dto.WordData;

import java.util.List;


public interface RepeatNoteDAO extends JpaRepository<RepeatNote, Integer>{

	// JPQL 사용하여 SELECT. FROM 절 등에 Entity(Class) 를 사용해야함에 주의
	@Query("SELECT r.wordData FROM RepeatNote r WHERE r.member.memberNo = :memberNo ORDER BY r.wordData.wordId DESC")
	public List<WordData> findWordDataByMemberNo(@Param("memberNo") int memberNo);
}
