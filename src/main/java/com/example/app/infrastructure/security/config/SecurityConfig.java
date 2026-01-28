package com.example.app.infrastructure.security.config;

import com.example.app.infrastructure.security.jwt.JwtAuthenticationEntryPoint;
import com.example.app.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.example.app.infrastructure.security.oauth.CustomOAuth2UserService;
import com.example.app.infrastructure.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Main Spring Security Configuration
 * Configures stateless JWT authentication and OAuth2 login
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CorsConfigurationSource corsConfigurationSource;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (using JWT tokens)
                .csrf(AbstractHttpConfigurer::disable)
                
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                
                // Stateless session management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Exception handling
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        
                        // Public room search endpoint
                        .requestMatchers(HttpMethod.GET, "/api/v1/rooms/search").permitAll()
                        
                        // Swagger/OpenAPI endpoints
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        
                        // Actuator endpoints
                        .requestMatchers("/actuator/health").permitAll()
                        
                        // OPTIONS requests (CORS preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                
                // OAuth2 Login configuration
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                
                // Add JWT filter
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
