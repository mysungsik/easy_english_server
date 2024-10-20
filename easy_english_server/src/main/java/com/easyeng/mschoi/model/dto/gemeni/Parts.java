package com.easyeng.mschoi.model.dto.gemeni;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parts {
    @JsonProperty("text")
    private String text;
}