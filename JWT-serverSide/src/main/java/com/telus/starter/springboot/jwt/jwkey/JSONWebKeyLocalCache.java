package com.telus.starter.springboot.jwt.jwkey;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A local cache of JSON Web Ket sets.
 *   
 * @author mentchar
 *
 */

public class JSONWebKeyLocalCache {
	
	private Logger logger = LoggerFactory.getLogger(JSONWebKeyLocalCache.class);
	
	private ThreadLocal<String> currentJku = new ThreadLocal<String>();
	
	
	/**
	 * Move this to something more robust like ehcache?
	 */
	private Map<String, JWKSet> cachedJwk = new HashMap<String, JWKSet>();
	
	private JSONWebKeyLocalCache() {
		
	}
	
	private static JSONWebKeyLocalCache instance = new JSONWebKeyLocalCache();
	
	public static JSONWebKeyLocalCache getInstance() {
		return instance;
	}
	
	public JWKSet getKeySet(String jku) {
		
		//Populate the thread local JKU with the current one requested.
		currentJku.set(jku);
		
		if (!cachedJwk.containsKey(jku)) {
			logger.debug("retrieve JWK from the server...");
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<JWKSet> result = null;
			try {
			    System.setProperty("https.proxyHost", "pac.tsl.telus.com");
			    System.setProperty("https.proxyPort", "8080");				
				result = restTemplate.exchange(jku, HttpMethod.GET, null, JWKSet.class);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			logger.debug("JWKKeySet::" + result.getBody());
			
			logger.debug("Populating the cached JWK from the server...");
			putKey(jku, result.getBody());
		}
		return cachedJwk.get(jku);
	}
	public JWKSet getKeySet_proxy(String jku) {

		// Populate the thread local JKU with the current one requested.
		currentJku.set(jku);

		if (!cachedJwk.containsKey(jku)) {
			logger.debug("Populating the cached JWK from the server...");
			return callServiceToGetKeySet(jku);
		}
		return cachedJwk.get(jku);
	}
	

	/**
	 * Take the JKU to be refreshed from the thread local currentJku value.
	 * The io.jsonwebtoken library does not expose an capability to get
	 * just the JKU from the JWS.
	 * 
	 * TODO: Provide a capability to extract the JKU from the token
	 * 
	 * @return
	 */
	public JWKSet refreshKeySet() {
		if (currentJku.get() != null) {
			String jku = currentJku.get(); 
			logger.debug("Refreshing JWK using thread local JKU [" + jku + "]");
			return refreshKeySet(jku);
		} else {
			throw new RuntimeException("Thread local JKU value not set");
		}
	}
	
	public JWKSet refreshKeySet(String jku) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JWKSet> result = restTemplate.exchange(jku, HttpMethod.GET, null, JWKSet.class);
		
		logger.debug("JWKKeySet::" + result.getBody());
		putKey(jku, result.getBody());
		
		return cachedJwk.get(jku);
	}
	
	
	public void putKey(String jku, JWKSet keySet) {
		cachedJwk.put(jku, keySet);
	}

	public JWKSet callServiceToGetKeySet(String jku) {
		RestTemplate restTemplate = new RestTemplate(getSimpleClientHttpRequestFactory());
		ResponseEntity<JWKSet> result = null;
		try {
			result = restTemplate.exchange(jku, HttpMethod.GET, null, JWKSet.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		putKey(jku, result.getBody());
		return cachedJwk.get(jku);
	}
	
	private SimpleClientHttpRequestFactory getSimpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("pac.tsl.telus.com", Integer.parseInt("8080")));
		requestFactory.setProxy(proxy);

		return requestFactory;
	}	
}
