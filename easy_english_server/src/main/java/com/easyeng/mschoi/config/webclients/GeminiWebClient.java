package com.easyeng.mschoi.config.webclients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Qualifier("geminiWebClient")
public class GeminiWebClient implements WebClientInterface{

	@Value("${project.geminiapiKey}")
	private String geminiKey;

	@Override
	public WebClient createWebClient() {
		StringBuilder baseUrl= new StringBuilder();
		baseUrl.append("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=");
		baseUrl.append(geminiKey);
		
		return WebClient.builder()
				.baseUrl(baseUrl.toString())
				.defaultHeader("Content-Type", "application/json")
				.build();
	}
	
}
