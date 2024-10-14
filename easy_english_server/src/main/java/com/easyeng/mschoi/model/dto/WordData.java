package com.easyeng.mschoi.model.dto;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="word_data")
public class WordData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "word_id")
    private Integer wordId;

    @Column(name = "word_level", nullable = false)
    @ColumnDefault("1")
    private Integer wordLevel = 1;

    @Column(name = "word_spell", length = 400, nullable = false)
    private String wordSpell;

    @Column(name = "word_mean", length = 400)
    private String wordMean;

    @Column(name = "example_sentence", length = 500)
    private String exampleSentence;

    @Column(name = "example_mean", length = 500)
    private String exampleMean;
	
}
