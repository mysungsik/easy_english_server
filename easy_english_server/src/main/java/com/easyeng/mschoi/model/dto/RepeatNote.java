package com.easyeng.mschoi.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(
		name = "repeat_note",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"member_no", "word_id"})
		})
public class RepeatNote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repeat_no")
	private Integer repeatNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "member_no", referencedColumnName = "member_no", nullable = false)
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "word_id", referencedColumnName = "word_id", nullable = false)
	private WordData wordData;
	
	@Transient // DB에 저장하지 않음. 단순 계산용 필드역할
	private int memberNo;
	
	@Transient
	private int wordId;
	
	@PrePersist // DB 저장 직전에 실행되는 메서드
	public void prePersist () {
		this.member = new Member();
		member.setMemberNo(this.memberNo);
		
		this.wordData = new WordData();
		wordData.setWordId(this.wordId);
	}
}
