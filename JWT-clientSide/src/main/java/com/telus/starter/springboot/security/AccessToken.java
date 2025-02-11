package com.telus.starter.springboot.security;

import java.time.LocalDateTime;

public class AccessToken {
	
	private String accessToken;
	private String tokenType;
	private Long expiresIn;
	private LocalDateTime fetchedLocalDateTime;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public LocalDateTime getFetchedLocalDateTime() {
		return fetchedLocalDateTime;
	}
	public void setFetchedLocalDateTime(LocalDateTime fetchedLocalDateTime) {
		this.fetchedLocalDateTime = fetchedLocalDateTime;
	}
	
	

}
