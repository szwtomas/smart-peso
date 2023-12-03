package com.smartpeso.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.function.Function;
import java.util.Date;

@Service
public class JwtService {
    private final static long ACCESS_TOKEN_DURATION_MILLIS = 1000 * 60 * 24;
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        return subject.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateAccessToken(String email) {
        return buildToken(email, ACCESS_TOKEN_DURATION_MILLIS);
    }

    private String buildToken(String email, long durationMillis) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + durationMillis))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        String secretKey = "8B887EAA238F79D1E117EA8BBBCED515396CD2ABB9172D476F24F1D432"; // TODO: Change this!!!
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
