package com.telus.starter.springboot.security;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TfmApiGatewayAdapter {

	private static Logger log = LoggerFactory.getLogger(TfmApiGatewayAdapter.class);

	
	@Value("${security.accesstoken.username}")
	private String userName;
	
	@Value("${security.accesstoken.password}")
	private String password;
	
	@Value("${security.accesstoken.apigw-token-url}")
	private String apigwTokenUrl;
	
	@Value("${security.accesstoken.tfm_project_id}")
	private String tfm_project_id;	
	
	private AccessToken latestAccessToken;
	
	public AccessToken requestAccessToken() throws JsonProcessingException, IOException {
		if (isAccessTokenExpired()) {
			//  get a new  access token (jwt)	
			return getApiGWAccessToken();				
		}							
		return getLatestOauth2AccessToken();
	}


	private AccessToken getApiGWAccessToken() {
		//headers
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		
		String credentials = userName + ":" + password;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		//set parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "client_credentials");
			map.add("scope", tfm_project_id);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		ResponseEntity<String> response = null;
		try {
			//proxy added for timeout error during local testing
		    System.setProperty("https.proxyHost", "pac.tsl.telus.com");
		    System.setProperty("https.proxyPort", "8080");		
		    //invoke api gw token svc
			response = restTemplate.exchange(apigwTokenUrl, HttpMethod.POST, request, String.class);
		} catch (RestClientException e1) {
			e1.printStackTrace();
			throw new SecurityException("getting AccessToken from ApiGW <" + apigwTokenUrl + "> failed. " + e1.getMessage() , e1);			
		} catch (Throwable t) {
			t.printStackTrace();
			throw new SecurityException("getting AccessToken from ApiGW <" + apigwTokenUrl + "> failed. " + t.getMessage() , t);		
		}
		
		java.time.LocalDateTime inst = java.time.LocalDateTime.now();
		HttpStatus status = response.getStatusCode();
		if (!HttpStatus.OK.equals(status)) {
			throw new SecurityException(response.toString() + ":"  + response.getStatusCodeValue() +  "," + response.toString() + ":" + response.getBody());
		}
//refresh_token
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode node = mapper.readTree(response.getBody());
			AccessToken oauth2AccessToken = new AccessToken();
			oauth2AccessToken.setAccessToken(node.path("access_token").asText());
			oauth2AccessToken.setTokenType(node.path("token_type").asText());
			oauth2AccessToken.setExpiresIn(node.path("expires_in").asLong());
			oauth2AccessToken.setFetchedLocalDateTime(inst);
			setLatestOauth2AccessToken(oauth2AccessToken);
			return oauth2AccessToken;
		} catch (Exception e) {
			throw new SecurityException("Failed to parse access token: " + response.toString() + ":" + response.getBody(), e);
		}		

	}

	
	private Boolean isAccessTokenExpired() {
		Long expiresIn;
		java.time.LocalDateTime fetchedLocalTime;
		java.time.LocalDateTime tokenExpireTime;
		
		AccessToken latestAccessToken = getLatestOauth2AccessToken();
		if (latestAccessToken != null) {
			expiresIn = latestAccessToken.getExpiresIn();
			fetchedLocalTime = latestAccessToken.getFetchedLocalDateTime();
			tokenExpireTime = fetchedLocalTime.plusSeconds(expiresIn);
			if (java.time.LocalDateTime.now().isAfter(tokenExpireTime)) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}	

	public AccessToken getLatestOauth2AccessToken() {
		return latestAccessToken;
	}

	public void setLatestOauth2AccessToken(AccessToken latestOauth2AccessToken) {
		this.latestAccessToken = latestOauth2AccessToken;
	}

	
	
}
