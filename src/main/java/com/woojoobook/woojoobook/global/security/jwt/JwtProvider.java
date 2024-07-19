package com.woojoobook.woojoobook.global.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtProvider {

	@Value("${jwt.secret.key}")
	private String secretString;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
			.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("authorities").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return true;
		} catch(SecurityException | MalformedJwtException e){
			log.info("token: {}", token);
			log.info("log = {}, {}", e.getClass(), e.getMessage());
			log.info("잘못된 JWT 서명입니다.");
		} catch(ExpiredJwtException e){
			log.info("만료된 JWT 토큰입니다.");
		} catch(UnsupportedJwtException e){
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch(IllegalArgumentException e){
			log.info("JWT 토큰이 잘못되었습니다.");
		}

		return false;
	}
}