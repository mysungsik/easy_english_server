package com.easyeng.mschoi.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyeng.mschoi.model.dto.RepeatNote;
import com.easyeng.mschoi.model.dto.WordData;

import java.util.List;


public interface RepeatNoteDAO extends JpaRepository<RepeatNote, Integer>{

	
	/** memberNo 와 WordId 로 복습단어장 확인
	 * @param memberNo
	 * @param wordId
	 * @return
	 */
	@Query("SELECT COUNT(r) FROM RepeatNote r WHERE r.member.memberNo = :memberNo AND r.wordData.wordId = :wordId")
	public Integer findRepeatNoteByMemberNoAndWordId(@Param("memberNo") int memberNo, @Param("wordId") int wordId);
	
	/** 전체 단어장 데이터 가져오기
	 * @param memberNo
	 * @return
	 * 
	 * JPQL 사용하여 SELECT. FROM 절 등에 Entity(Class) 를 사용해야함에 주의
	 */
	@Query("SELECT r.wordData FROM RepeatNote r WHERE r.member.memberNo = :memberNo ORDER BY r.wordData.wordId DESC")
	public List<WordData> findWordDataByMemberNo(@Param("memberNo") int memberNo);

	/** memberNo 와 WordId 로 복습단어장 삭제
	 * @param memberNo
	 * @param wordId
	 * @return
	 */
	@Modifying
	@Query("DELETE FROM RepeatNote r WHERE r.member.memberNo = :memberNo AND r.wordData.wordId = :wordId")
	public Integer deleteRepeatNoteByMemberNoAndWordId(@Param("memberNo") int memberNo, @Param("wordId") int wordId);
}
