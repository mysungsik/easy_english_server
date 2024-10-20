package com.easyeng.mschoi.model.dto.gemeni;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyRating {
    @JsonProperty("category")
    private String category;

    @JsonProperty("probability")
    private String probability;
}

