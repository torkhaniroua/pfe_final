package com.application.configuration;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.application.services.CustomUserDetailsService;
import com.application.security.CustomOAuth2UserService;
import com.application.security.OAuth2LoginFailureHandler;
import com.application.security.OAuth2LoginSuccessHandler;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtAuthorizationFilter jwtAuthorizationFilter,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          OAuth2LoginFailureHandler oAuth2LoginFailureHandler) {
        this.userDetailsService = customUserDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bcryptPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/register**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/login/oauth2/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/oauth/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/oauth/callback/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/professors/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/quiz/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/addCourse")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/course/**")).hasAnyAuthority("ADMIN", "PROFESSOR")                    .requestMatchers(new AntPathRequestMatcher("/course/**")).permitAll()
                    // Allow unauthenticated access to the legacy course listing endpoint used by the frontend
                    .requestMatchers(new AntPathRequestMatcher("/listcourses")).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(cfg -> cfg.userService(customOAuth2UserService))
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler)
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Correct way
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
               
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Frontend origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
