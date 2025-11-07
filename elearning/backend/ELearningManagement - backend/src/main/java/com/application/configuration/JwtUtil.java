package com.application.configuration;

import io.jsonwebtoken.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.application.model.Professor;
import com.application.model.User;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private final String secret_key = "Ttx4xQQEXASM/TE4SqdosSxfyrpbSXibmZwxx5nnnDE=";
    private final long accessTokenValidity = 60 * 60 * 1000; // 1 heure

    private final JwtParser jwtParser;
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        // Allow a small clock skew (60 seconds) to tolerate minor time differences
        this.jwtParser = Jwts.parser()
                .setSigningKey(secret_key)
                .setAllowedClockSkewSeconds(60);
    }

    // ✅ Unifie User et Professor pour éviter le code dupliqué
    public String createToken(String email, String mobile) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("email", email);
        claims.put("mobile", mobile);
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + accessTokenValidity);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    // Appels spécifiques
    public String createToken(User user) {
        return createToken(user.getEmail(), user.getMobile());
    }

    public String createToken(Professor prof) {
        return createToken(prof.getEmail(), prof.getMobile());
    }

    // ============================ UTILITAIRES ============================

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
