package com.application.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.oauth2.frontend-error:http://localhost:4200/login}")
    private String errorRedirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = exception == null ? "Unable to authenticate with provider" : exception.getMessage();
        String encodedMessage = UriUtils.encode(errorMessage, StandardCharsets.UTF_8);
        String targetUrl = UriComponentsBuilder.fromUriString(errorRedirectUri)
                .queryParam("error", encodedMessage)
                .build(true)
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
