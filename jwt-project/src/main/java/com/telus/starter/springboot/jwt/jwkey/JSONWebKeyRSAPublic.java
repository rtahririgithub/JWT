/*
 package com.telus.starter.springboot.jwt.jwkey;


public class JSONWebKeyRSAPublic extends JSONWebKey {

	private static final long serialVersionUID = 1L;

	public JSONWebKeyRSAPublic() {
		// TODO Auto-generated constructor stub
	}

	public JSONWebKeyRSAPublic(String kty, String use, String key_ops, String alg, String kid, String x5u, String[] x5c,
			String x5t, String x5t256, String n, String e) {
		super(kty, use, key_ops, alg, kid, x5u, x5c, x5t, x5t256);
		this.n = n;
		this.e = e;
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((e == null) ? 0 : e.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONWebKeyRSAPublic other = (JSONWebKeyRSAPublic) obj;
		if (e == null) {
			if (other.e != null)
				return false;
		} else if (!e.equals(other.e))
			return false;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JSONWebKeyRSAPublic [n=" + n + ", e=" + e + ", toString()=" + super.toString() + "]";
	}

}
 */