package com.easyeng.mschoi.config.webclients;

import org.springframework.web.reactive.function.client.WebClient;

public interface WebClientInterface {

	WebClient createWebClient();
}
