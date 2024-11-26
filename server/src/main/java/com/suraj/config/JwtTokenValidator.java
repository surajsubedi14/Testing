package com.suraj.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		System.out.println("Received JWT: " + jwt);

		if (jwt != null && jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
			System.out.println("Token without Bearer: " + jwt);
			try {
				validateTokenAndAuthenticate(jwt);
			} catch (BadCredentialsException e) {
				System.out.println("Bad credentials: " + e.getMessage());
				// Optionally, you can handle this error more gracefully
				// For example, by setting the response status and message
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid token");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private void validateTokenAndAuthenticate(String jwt) {
		try {
			SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

			String email = claims.get("email", String.class);
			String authorities = claims.get("authorities", String.class);

			List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
			Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (ExpiredJwtException e) {
			System.out.println("Expired JWT token: " + e.getMessage());
			throw new BadCredentialsException("Expired token", e);
		} catch (UnsupportedJwtException e) {
			System.out.println("Unsupported JWT token: " + e.getMessage());
			throw new BadCredentialsException("Unsupported token", e);
		} catch (MalformedJwtException e) {
			System.out.println("Malformed JWT token: " + e.getMessage());
			throw new BadCredentialsException("Malformed token", e);
		} catch (SignatureException e) {
			System.out.println("Invalid JWT signature: " + e.getMessage());
			throw new BadCredentialsException("Invalid signature", e);
		} catch (IllegalArgumentException e) {
			System.out.println("JWT token compact of handler are invalid: " + e.getMessage());
			throw new BadCredentialsException("Invalid token", e);
		}
	}
}
