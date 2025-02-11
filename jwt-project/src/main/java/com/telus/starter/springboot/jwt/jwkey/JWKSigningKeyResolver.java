package com.telus.starter.springboot.jwt.jwkey;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.telus.starter.springboot.config.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWKSigningKeyResolver extends SigningKeyResolverAdapter {
	
	private Logger logger = LoggerFactory.getLogger(JWKSigningKeyResolver.class);
	
	@Value( "${management.security.enabled}" )
	private String enabled;
	
	@Value("${security.oauth2.jwkKeySetUrl}")
	private String jwkKeySetUrl;


	@Value("${spring.profiles}")
	private String profiles;
		  
	
	public JWKSigningKeyResolver() {
		// TODO Auto-generated constructor stub
	}
	

	
	public void refreshKey() {
		
	}
	
	//validate the signature
	@Override
	public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) throws UnsupportedJwtException {

		//Call/cache the json web key store (jwks endpoint) to get the list of JSON Web Keys. 
		logger.info("reza jwkKeySetUrl=" + jwkKeySetUrl);
		
		JWKSet jwKeySet = JSONWebKeyLocalCache.getInstance().getKeySet(jwkKeySetUrl);
		String headerKeyId = jwsHeader.getKeyId();

		//Lookup the jwKey(jwk) by given keyId ( kid from (jwt) request header.
		JSONWebKey jwKey = findKey(jwKeySet, headerKeyId);		
		
		//create key specification using attributes n and e from the JWK
		byte[] modulus = Base64.getUrlDecoder().decode(jwKey.getN());
		byte[] exponent = Base64.getUrlDecoder().decode(jwKey.getE());		
		

		//since java .8.0_161 modulus shouldn't be negative hence  build BigInteger like BigInteger(1, modulus)to be positive:
		RSAPublicKeySpec spec = new RSAPublicKeySpec(
				new BigInteger(1,modulus), 
				new BigInteger(1,exponent));
		
		//Generates a public key object from the provided key specification (key material).
		KeyFactory kf = null;
		PublicKey pk = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			pk = kf.generatePublic(spec);
		} catch (NoSuchAlgorithmException e1) {
			logger.error("resolveSigningKey:Error generating RSK Public Key",e1);
		} catch (InvalidKeySpecException e) {
			logger.error("resolveSigningKey:Error generating RSK Public Key",e);
		}catch (Throwable e) {
			logger.error("resolveSigningKey:Error generating RSK Public Key",e);
		}
		return pk;
	}
	
	private JSONWebKey findKey(JWKSet keySet, String keyId) {
		List<JSONWebKey> keys = keySet.getKeys();
		for (JSONWebKey jsonWebKey : keys) {
			if (jsonWebKey.getKid().equals(keyId)) {
				return (JSONWebKey)jsonWebKey;
			}
		}
		return null;
	}

}
