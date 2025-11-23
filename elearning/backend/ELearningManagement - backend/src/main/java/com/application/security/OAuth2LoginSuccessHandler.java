package com.application.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.application.configuration.JwtUtil;
import com.application.model.User;
import com.application.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Value("${app.oauth2.frontend-success:http://localhost:4200/oauth/callback}")
    private String successRedirectUri;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserService userService, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        String email = resolveEmail(oAuth2User, provider);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Provider " + provider + " did not return an email address");
        }
        String fullName = resolveName(oAuth2User, provider);
        String avatarUrl = resolveAvatar(oAuth2User, provider);

        User savedUser = userService.upsertOAuthUser(email, fullName, avatarUrl, provider);
        String token = jwtUtil.createToken(savedUser);

        Map<String, Object> payload = new HashMap<>();
        payload.put("user", sanitizeUser(savedUser));
        payload.put("token", token);
        payload.put("role", Optional.ofNullable(savedUser.getRole()).orElse("USER").toUpperCase());

        String payloadJson = objectMapper.writeValueAsString(payload);
        String encodedPayload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String targetUrl = UriComponentsBuilder.fromUriString(successRedirectUri)
                .queryParam("payload", encodedPayload)
                .queryParam("provider", provider)
                .build(true)
                .toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User sanitizeUser(User source) {
        User safe = new User();
        safe.setEmail(source.getEmail());
        safe.setUsername(source.getUsername());
        safe.setUserid(source.getUserid());
        safe.setMobile(source.getMobile());
        safe.setGender(source.getGender());
        safe.setProfession(source.getProfession());
        safe.setAddress(source.getAddress());
        safe.setRole(source.getRole());
        safe.setIsPremuim(source.getIsPremuim());
        safe.setAvatarUrl(source.getAvatarUrl());
        safe.setPassword("");
        return safe;
    }

    private String resolveEmail(OAuth2User user, String provider) {
        Object emailAttr = user.getAttribute("email");
        if (emailAttr != null) {
            return emailAttr.toString();
        }

        if ("github".equalsIgnoreCase(provider)) {
            Object emails = user.getAttribute("emails");
            if (emails instanceof Iterable) {
                for (Object val : (Iterable<?>) emails) {
                    if (val instanceof Map && ((Map<?, ?>) val).get("email") != null) {
                        return ((Map<?, ?>) val).get("email").toString();
                    }
                }
            }
        }

        return null;
    }

    private String resolveName(OAuth2User user, String provider) {
        Object nameAttr = user.getAttribute("name");
        if (nameAttr != null) {
            return nameAttr.toString();
        }

        if ("google".equalsIgnoreCase(provider)) {
            Object given = user.getAttribute("given_name");
            Object family = user.getAttribute("family_name");
            if (given != null || family != null) {
                return (given == null ? "" : given.toString()) + " " + (family == null ? "" : family.toString());
            }
        }

        if ("linkedin".equalsIgnoreCase(provider)) {
            Object first = user.getAttribute("localizedFirstName");
            Object last = user.getAttribute("localizedLastName");
            if (first != null || last != null) {
                return (first == null ? "" : first.toString()) + " " + (last == null ? "" : last.toString());
            }
        }

        Object login = user.getAttribute("login");
        if (login != null) {
            return login.toString();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private String resolveAvatar(OAuth2User user, String provider) {
        Object picture = user.getAttribute("picture");
        if (picture instanceof String) {
            return picture.toString();
        }

        if ("github".equalsIgnoreCase(provider)) {
            Object avatar = user.getAttribute("avatar_url");
            if (avatar != null) {
                return avatar.toString();
            }
        }

        if ("linkedin".equalsIgnoreCase(provider)) {
            Map<String, Object> profilePicture = user.getAttribute("profilePicture");
            if (profilePicture != null) {
                Map<String, Object> displayImage = (Map<String, Object>) profilePicture.get("displayImage~");
                if (displayImage != null && displayImage.containsKey("elements")) {
                    List<Map<String, Object>> elements = (List<Map<String, Object>>) displayImage.get("elements");
                    if (elements != null && !elements.isEmpty()) {
                        Map<String, Object> first = elements.get(elements.size() - 1);
                        List<Map<String, Object>> identifiers = (List<Map<String, Object>>) first.get("identifiers");
                        if (identifiers != null && !identifiers.isEmpty()) {
                            Object identifier = identifiers.get(0).get("identifier");
                            if (identifier != null) {
                                return identifier.toString();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
