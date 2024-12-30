package me.vasujain.studentsyncapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpiration);
    }


    /**
     * Core token generation method that builds JWT tokens using the modern JJWT builder pattern.
     * This replaces the deprecated setClaims method with a more structured approach.
     */
    private String buildToken(UserDetails userDetails, long expiration){
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(expiration, ChronoUnit.MILLIS);

        Map<String,Object> claims = new HashMap<>();
        claims.put("roles",userDetails.getAuthorities().toString());

        SecretKey key = getSigningKey();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryInstant))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    /**
     * Creates a secure signing key from the secret key string.
     * This method ensures consistent key generation across the application.
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username from a token.
     * Uses the modern JJWT parser builder pattern.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generic method to extract any claim from the token.
     * Uses the modern JJWT parser builder pattern.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a token using the modern JJWT parser.
     * This version uses the new parser builder pattern and proper key handling.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // This replaces the deprecated setSigningKey
                .build()
                .parseSignedClaims(token)     // This replaces the deprecated parseClaimsJws
                .getPayload();                // This replaces the deprecated getBody
    }

    /**
     * Validates if a token is still valid for a given user.
     * Checks both the username match and token expiration.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if a token has expired by extracting its expiration date.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    /**
     * Extracts the expiration date from a token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
