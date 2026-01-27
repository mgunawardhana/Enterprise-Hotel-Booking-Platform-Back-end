package com.example.app.infrastructure.security.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom OAuth2 User Service to load user information from OAuth2 provider
 */
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        log.info("OAuth2 user loaded: {}", oAuth2User.getAttribute("email"));
        
        // Additional processing can be done here if needed
        // For now, we just return the OAuth2User as-is
        // The actual user creation/update happens in OAuth2AuthenticationSuccessHandler
        
        return oAuth2User;
    }
}
