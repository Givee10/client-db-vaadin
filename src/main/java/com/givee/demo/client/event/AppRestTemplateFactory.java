package com.givee.demo.client.event;

import com.givee.demo.client.domain.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
public class AppRestTemplateFactory {
	@Value("${controller.address}")
	private String serverAddress;

	@Autowired
	private RestTemplateBuilder builder;

	public RestOperations createRestOperations() {
		return builder.build();
	}

	public RestOperations createRestOperations(LoginDto loginDto) {
		return builder.basicAuthorization(loginDto.getUsername(), loginDto.getPassword()).build();
	}

	public String getServerAddress() {
		return serverAddress;
	}
}
