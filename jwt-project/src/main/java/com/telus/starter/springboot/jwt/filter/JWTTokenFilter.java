package com.telus.starter.springboot.jwt.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.telus.starter.springboot.jwt.jwkey.JSONWebKeyLocalCache;
import com.telus.starter.springboot.jwt.jwkey.JWKSigningKeyResolver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
@Component
public class JWTTokenFilter extends GenericFilterBean {
	
	private Logger logger = LoggerFactory.getLogger(JWTTokenFilter.class);	
	@Value( "${management.security.enabled}" )
	private String enabled;
	
	//JWTTokenProvider jwtTokenProvider;
	
	@Autowired
	JWKSigningKeyResolver keyResolver;
	
	@Value("${security.oauth2.scope}")
	private String scope;
	@Value("${security.oauth2.audience}")
	private String audience;
	
		
	public JWTTokenFilter() {

	}
	

	 

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String bearerToken = null;
		String authorizationHeader = ((HttpServletRequest)request).getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	bearerToken=authorizationHeader.substring(7, authorizationHeader.length());
        }else {
        	//throw auth exception: missing Authorization header
        }
       
        logger.debug("Reza bearerToken=" + bearerToken);
 
		if (bearerToken != null) {
			Jws<Claims> jws = null;
			JwtParser jwsParser ;
			try {
				logger.debug("JWTTokenFilter validating token [" + bearerToken + "]");
				jwsParser = Jwts.parser();
				jwsParser.setSigningKeyResolver(keyResolver);
				jwsParser.setAllowedClockSkewSeconds(500);				
				try {			 
					 jws =jwsParser.parseClaimsJws(bearerToken);
				} catch (SignatureException e) {
					//in case key rotation in key store server has happens, the cached key is not longer valid.  we need to get the new key form the key store server. 
					//Reload the cache and try again
					logger.warn("Invalid JWK, refreshing cached instance and retrying...");
					JSONWebKeyLocalCache.getInstance().refreshKeySet();
					try {
						jws =jwsParser.parseClaimsJws(bearerToken);
					} catch (SignatureException e2) {
						throw new Exception(e2.getMessage());
					}
					 
				} catch (Throwable t) {
					t.printStackTrace();
					throw new Exception(t.getMessage());
				}
				
				if(jws==null) {
					throw new Exception("Failed to parse and get the Claims JWS");
				}
				validateJwsClaims(jws);
				
				
				chain.doFilter(request, response);
				
			}  catch (Exception e) {
				logger.error("Error in validating JWT Token [" + bearerToken + "]", e);
				((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to validate JWT Token." + e.getMessage());
			}
		} else {
			logger.error("JWTTokenFilter: Authentication token missing");
			//((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token missing");
		}
		
		chain.doFilter(request, response);
	}
	private void validateJwsClaims(Jws<Claims> jws) throws Exception {
		if (null != jws) {
			ArrayList<String> jwsScope = (ArrayList<String>) jws.getBody().get("scope");
			if (!jwsScope.contains(scope)) {
				throw new Exception("The scope: " + scope + " is not in the token.");
			}

			String aud = jws.getBody().getAudience();
			if (!aud.equals(audience)) {
				throw new Exception("The Audience:" + audience + " is not in the token.");
			}
		} else {
			throw new Exception( "JWTTokenFilter: Authentication token missing.");
		}
	}

}
