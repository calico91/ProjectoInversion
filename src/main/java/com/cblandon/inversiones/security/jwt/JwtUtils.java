package com.cblandon.inversiones.security.jwt;

import com.cblandon.inversiones.user.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;
    final UserDetailsServiceImpl userDetailsService;

    public JwtUtils(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Generar token de acceso
    public String generateAccesToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(6, ChronoUnit.HOURS);
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .claim("Roles", roles)
                .setSubject(username)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar el token de acceso
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.error("invalid-token: ".concat(e.getMessage()));
            return false;
        }
    }

    // Obtener el username del token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Obtener un solo claim
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Obtener todos los claims del token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener firma del token
    public Key getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
