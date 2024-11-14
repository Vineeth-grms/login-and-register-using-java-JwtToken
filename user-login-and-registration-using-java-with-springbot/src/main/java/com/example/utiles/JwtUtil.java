package com.example.utiles;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).signWith(key).compact();

	}

	public String exctractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public Boolean isTokenExperied(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration()
				.before(new Date());

	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = exctractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExperied(token));
	}

}
