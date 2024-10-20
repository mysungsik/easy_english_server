package com.easyeng.mschoi.model.dto.gemeni;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponse {
    @JsonProperty("candidates")
    private List<Candidate> candidates;

}
