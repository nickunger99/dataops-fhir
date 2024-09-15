package com.dataops.api.domain.auth.security.jwt;

import com.dataops.api.domain.auth.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	@Value("${userauth.app.jwtSecret}")
	private String jwtSecret;

	@Value("${userauth.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${userauth.app.jwtExpirationMailMs}")
	private int jwtExpirationMailMs;

	@Value("${userauth.app.jwtCookieName}")
	private String jwtCookie;

	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromUsername(userPrincipal.getUsername());
		return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true)
				.build();
	}

	public ResponseCookie getCleanJwtCookie() {
		return ResponseCookie.from(jwtCookie, null).path("/api").build();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String generateTokenFromEmail(String email) {
		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMailMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	// public ResponseCookie generateJwtCookie(EmailRequest emailRequest) {
	// String jwt = generateTokenFromEmail(emailRequest.getEmail());
	// return ResponseCookie.from(jwtCookie,
	// jwt).path("/api/auth/reset_password").maxAge(24 * 60 * 60).httpOnly(true)
	// .build();
	// }

	// public ResponseCookie generateJwtCookie(String email) {
	// String jwt = generateTokenFromEmail(email);
	// return ResponseCookie.from(jwtCookie,
	// jwt).path("/api/auth/confirm_email").maxAge(24 * 60 * 60).httpOnly(true)
	// .build();
	// }
}