package com.application.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String LINKEDIN_PROVIDER = "linkedin";
    private static final String GITHUB_PROVIDER = "github";
    private final RestTemplate restTemplate;

    public CustomOAuth2UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        if (LINKEDIN_PROVIDER.equalsIgnoreCase(registrationId)) {
            Map<String, Object> enrichedAttributes = new HashMap<>(oAuth2User.getAttributes());
            String email = fetchLinkedInEmail(userRequest.getAccessToken().getTokenValue());
            if (email != null && !email.isBlank()) {
                enrichedAttributes.put("email", email);
            }
            return new DefaultOAuth2User(oAuth2User.getAuthorities(), enrichedAttributes, userNameAttribute);
        }

        if (GITHUB_PROVIDER.equalsIgnoreCase(registrationId)) {
            Map<String, Object> enrichedAttributes = new HashMap<>(oAuth2User.getAttributes());
            String email = fetchGithubPrimaryEmail(userRequest.getAccessToken().getTokenValue());
            if (email != null && !email.isBlank()) {
                enrichedAttributes.put("email", email);
            }
            return new DefaultOAuth2User(oAuth2User.getAuthorities(), enrichedAttributes, userNameAttribute);
        }

        return oAuth2User;
    }

    @SuppressWarnings("unchecked")
    @Retry(name = "oauthExternal")
    @CircuitBreaker(name = "oauthExternal", fallbackMethod = "emailFallback")
    private String fetchLinkedInEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("X-Restli-Protocol-Version", "2.0.0");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))",
                HttpMethod.GET,
                requestEntity,
                Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("elements")) {
            return null;
        }

        List<Map<String, Object>> elements = (List<Map<String, Object>>) body.get("elements");
        if (elements == null) {
            return null;
        }

        return elements.stream()
                .map(element -> (Map<String, Object>) element.get("handle~"))
                .filter(handle -> handle != null && handle.containsKey("emailAddress"))
                .map(handle -> handle.get("emailAddress"))
                .map(Object::toString)
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Retry(name = "oauthExternal")
    @CircuitBreaker(name = "oauthExternal", fallbackMethod = "emailFallback")
    private String fetchGithubPrimaryEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        List<Map<String, Object>> emails = response.getBody();
        if (emails == null) {
            return null;
        }

        return emails.stream()
                .filter(map -> Boolean.TRUE.equals(map.get("primary")))
                .map(map -> map.get("email"))
                .map(Object::toString)
                .findFirst()
                .orElseGet(() -> emails.stream()
                        .map(map -> map.get("email"))
                        .map(Object::toString)
                        .findFirst()
                        .orElse(null));
    }

    @SuppressWarnings("unused")
    private String emailFallback(String accessToken, Throwable ex) {
        // When external providers are slow or failing, degrade gracefully by skipping email enrichment.
        return null;
    }
}
