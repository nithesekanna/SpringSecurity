package com.example.security;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthenticationManager authManager;
	private final JwtService service;
	private final SessionTokenRepository sessionTokenRepository;
	
	@PostMapping("/login")
	public String postMethodName(@RequestBody LoginRequest request, HttpServletRequest req) {

		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
			
		    // 🔄 Deactivate any old tokens
		    sessionTokenRepository.findByUsernameAndActive(request.getUserName(),"Y").ifPresent(old -> {
		        old.setActive("N");
		        old.setExpiresAt(LocalDateTime.now());
		        sessionTokenRepository.save(old);
		    });

		    // ✅ Generate new token
		    String token = service.getJsonWebToken(request.getUserName());
		    SessionToken session = SessionToken.builder()
		        .username(request.getUserName())
		        .token(token)
		        .issuedAt(LocalDateTime.now())
		        .expiresAt(LocalDateTime.now().plusHours(2))
		        .active("Y")
		        .ipAddress(req.getRemoteAddr())
		        .build();

		    sessionTokenRepository.save(session);
			
			return token;
		} catch (AuthenticationException e) {
			return "Invalid credentials";
		}
	}
	
}
