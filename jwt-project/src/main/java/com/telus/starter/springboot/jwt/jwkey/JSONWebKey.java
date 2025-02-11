package com.telus.starter.springboot.jwt.jwkey;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/*
 * Implementation of a JSON Web Key
 * https://tools.ietf.org/html/rfc7517
 * 
 */
//@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "kty")
//@JsonSubTypes({@JsonSubTypes.Type(value=JSONWebKeyRSAPublic.class, name="RSA")})

public class JSONWebKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public JSONWebKey() {
		
	}
	
	public JSONWebKey(String kty, String use, String key_ops, String alg, String kid, String x5u, String[] x5c,
			String x5t, String x5t256,String n, String e) {
		super();
		this.kty = kty;
		this.use = use;
		this.key_ops = key_ops;
		this.alg = alg;
		this.kid = kid;
		this.x5u = x5u;
		this.x5c = x5c;
		this.x5t = x5t;
		this.x5t256 = x5t256;
		this.n = n;
		this.e = e;		
	}
	private String kty;
	private String use;
	private String key_ops;
	private String alg;
	private String kid;
	private String x5u;
	private String[] x5c;
	private String x5t;
	private String x5t256;
	private String n;
	private String e;

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}	
	public String getKty() {
		return kty;
	}
	public void setKty(String kty) {
		this.kty = kty;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getKey_ops() {
		return key_ops;
	}
	public void setKey_ops(String key_ops) {
		this.key_ops = key_ops;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getX5u() {
		return x5u;
	}
	public void setX5u(String x5u) {
		this.x5u = x5u;
	}
	public String[] getX5c() {
		return x5c;
	}
	public void setX5c(String[] x5c) {
		this.x5c = x5c;
	}
	public String getX5t() {
		return x5t;
	}
	public void setX5t(String x5t) {
		this.x5t = x5t;
	}
	public String getX5t256() {
		return x5t256;
	}
	public void setX5t256(String x5t256) {
		this.x5t256 = x5t256;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alg == null) ? 0 : alg.hashCode());
		result = prime * result + ((key_ops == null) ? 0 : key_ops.hashCode());
		result = prime * result + ((kid == null) ? 0 : kid.hashCode());
		result = prime * result + ((kty == null) ? 0 : kty.hashCode());
		result = prime * result + ((use == null) ? 0 : use.hashCode());
		result = prime * result + Arrays.hashCode(x5c);
		result = prime * result + ((x5t == null) ? 0 : x5t.hashCode());
		result = prime * result + ((x5t256 == null) ? 0 : x5t256.hashCode());
		result = prime * result + ((x5u == null) ? 0 : x5u.hashCode());

		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());		
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONWebKey other = (JSONWebKey) obj;
		if (alg == null) {
			if (other.alg != null)
				return false;
		} else if (!alg.equals(other.alg))
			return false;
		if (key_ops == null) {
			if (other.key_ops != null)
				return false;
		} else if (!key_ops.equals(other.key_ops))
			return false;
		if (kid == null) {
			if (other.kid != null)
				return false;
		} else if (!kid.equals(other.kid))
			return false;
		if (kty == null) {
			if (other.kty != null)
				return false;
		} else if (!kty.equals(other.kty))
			return false;
		if (use == null) {
			if (other.use != null)
				return false;
		} else if (!use.equals(other.use))
			return false;
		if (!Arrays.equals(x5c, other.x5c))
			return false;
		if (x5t == null) {
			if (other.x5t != null)
				return false;
		} else if (!x5t.equals(other.x5t))
			return false;
		if (x5t256 == null) {
			if (other.x5t256 != null)
				return false;
		} else if (!x5t256.equals(other.x5t256))
			return false;
		if (x5u == null) {
			if (other.x5u != null)
				return false;
		} else if (!x5u.equals(other.x5u))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "JSONWebKey [kty=" + kty + ", use=" + use + ", key_ops=" + key_ops + ", alg=" + alg + ", kid=" + kid
				+ ", x5u=" + x5u + ", x5c=" + Arrays.toString(x5c) + ", x5t=" + x5t + ", x5t256=" + x5t256 + "]";
	}

}
