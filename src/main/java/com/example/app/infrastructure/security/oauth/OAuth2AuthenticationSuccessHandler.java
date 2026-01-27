package com.example.app.infrastructure.security.oauth;

import com.example.app.domain.model.User;
import com.example.app.domain.port.in.AuthenticationUseCase;
import com.example.app.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Handler for successful OAuth2 authentication (Google login)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationUseCase authenticationUseCase;
    
    @Value("${app.oauth2.redirect-uri:http://localhost:3000/oauth2/redirect}")
    private String redirectUri;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", redirectUri);
            return;
        }
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        String sub = oAuth2User.getAttribute("sub"); // Google user ID
        
        log.info("OAuth2 login successful for email: {}", email);
        
        // Register or login user via OAuth2
        User user = authenticationUseCase.authenticateOAuth(email, name, "GOOGLE", sub, picture);
        
        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUsername(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList())
        );
        
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());
        
        // Redirect to frontend with tokens
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
