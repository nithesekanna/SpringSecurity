package com.example.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	private final JwtService jwtService;
	private final SessionTokenRepository repository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2User auth2User = (OAuth2User) authentication.getPrincipal();
		
		String email = auth2User.getAttribute("email");
		
		String jsonWebToken = jwtService.getJsonWebToken(email);
		
		// 🔄 Deactivate any old tokens
		repository.findByUsernameAndActive(email,"Y").ifPresent(old -> {
	        old.setActive("N");
	        old.setExpiresAt(LocalDateTime.now());
	        repository.save(old);
	    });
		
		// ✅ Store session token
        SessionToken session = SessionToken.builder()
                .username(email)
                .token(jsonWebToken)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(2))
                .active("Y")
                .ipAddress(request.getRemoteAddr())
                .build();

        repository.save(session);

        // ✅ Return token in response
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + jsonWebToken + "\"}");
		
	}

}
