package com.easyeng.mschoi.model.dto.gemeni;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @JsonProperty("content")
    private Content content;

    @JsonProperty("finishReason")
    private String finishReason;

    @JsonProperty("index")
    private int index;

    @JsonProperty("safetyRatings")
    private List<SafetyRating> safetyRatings;

}