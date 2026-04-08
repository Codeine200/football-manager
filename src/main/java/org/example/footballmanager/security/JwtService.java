package org.example.footballmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.footballmanager.config.JwtProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;

    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(getSignKey(jwtProperties.getAccessSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .signWith(getSignKey(jwtProperties.getRefreshSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessTokenFromRefresh(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = extractUsernameFromRefreshToken(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        return generateAccessToken(user);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token, jwtProperties.getAccessSecret()).getSubject();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractAllClaims(token, jwtProperties.getRefreshSecret()).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token, jwtProperties.getAccessSecret());
        List<String> roles = (List<String>)claims.get("roles");

        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isAccessTokenValid(String token) {
        return !isTokenExpired(token, jwtProperties.getAccessSecret());
    }

    public boolean isRefreshTokenValid(String token) {
        return !isTokenExpired(token, jwtProperties.getRefreshSecret());
    }

    private boolean isTokenExpired(String token, String secret) {
        return extractAllClaims(token, secret)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
