package com.easyeng.mschoi.config.webclients;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Qualifier("naverWebClient")
public class NaverWebClient  implements WebClientInterface{
	
	@Value("${project.naverClientID}")
	private String naverClientID;
	
	@Value("${project.naverClientSecret}")
	private String naverClientSecret;
    

	@Override
	public WebClient createWebClient() {
    	StringBuilder baseUrl = new StringBuilder();
    	baseUrl.append("https://openapi.naver.com/v1/search/encyc");
    	
    	System.out.println(baseUrl);
    	
    	return WebClient.builder()
    			.baseUrl(baseUrl.toString())
    			.defaultHeader("Content-Type", "application/json")
    			.defaultHeader("X-Naver-Client-Id", naverClientID)
    			.defaultHeader("X-Naver-Client-Secret", naverClientSecret)
    			.build();
	}
}

