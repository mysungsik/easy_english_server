package com.easyeng.mschoi.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Integer memberNo;

    @Column(name = "member_id", length = 30, nullable = false)
    private String memberId;

    @Column(name = "member_pw", length = 400, nullable = false)
    private String memberPw;

    @Column(name = "member_email", length = 100, nullable = false)
    private String memberEmail;

    @Column(name = "member_nickname", length = 100, nullable = false)
    private String memberNickname;

    @Column(name = "member_profile", length = 500)
    private String memberProfile;

    @Column(name = "member_created_dt", nullable = false)
    @ColumnDefault("(current_date)")
    private LocalDate memberCreatedDt = LocalDate.now();

    @Column(name = "member_deleted_dt")
    @ColumnDefault("(current_date)")
    private LocalDate memberDeletedDt;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = true)
    private WordData wordData;

    @Column(name = "word_level")
    private Integer wordLevel;
}