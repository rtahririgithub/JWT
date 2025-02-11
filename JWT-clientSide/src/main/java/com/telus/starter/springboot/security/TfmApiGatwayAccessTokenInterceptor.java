package com.telus.starter.springboot.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;


@Component
public class TfmApiGatwayAccessTokenInterceptor implements ClientHttpRequestInterceptor{

	private static Logger log = LoggerFactory.getLogger(TfmApiGatwayAccessTokenInterceptor.class);
	
	@Autowired
	private TfmApiGatewayAdapter tfmApiGatewayAdapter;


	
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		AccessToken tfmApiGwAccessToken = tfmApiGatewayAdapter.requestAccessToken();
		request.getHeaders().add("Authorization", "Bearer " + tfmApiGwAccessToken.getAccessToken());
		request.getHeaders().remove("Content-Type");
		request.getHeaders().add("Content-Type", "application/json; charset=utf-8");
		 ClientHttpResponse response = execution.execute(request, body);
	     return response;
	}

}
