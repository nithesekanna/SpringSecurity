package com.example.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "thisisthesecretkeyforjsonwebtoken123";
	
	private static final long EXPERATION_TIME = 1000 * 60 * 60;
	
	private Key getKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}
	
	public String getJsonWebToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPERATION_TIME))
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();

	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build()
				.parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getKey()).build()
			.parseClaimsJws(token);
			return true;
		}catch(JwtException e) {
			return false;
		}
	}
}
