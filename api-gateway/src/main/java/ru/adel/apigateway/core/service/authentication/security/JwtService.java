package ru.adel.apigateway.core.service.authentication.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.adel.apigateway.core.service.authentication.db.refresh_token.entity.RefreshToken;
import ru.adel.apigateway.core.service.authentication.db.user.entity.User;
import ru.adel.apigateway.core.service.authentication.security.details.UserDetailsImpl;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${jwt.refresh-token.lifetime}")
    private Duration refreshTokenLifetime;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateAccessToken(User user){
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiredAt = new Date(issuedAt.getTime()+accessTokenLifetime.toMillis());
        Map<String, Object> claims = Map.of("role", user.getRole());
        return buildToken(claims, UserDetailsImpl.of(user), issuedAt, expiredAt);
    }

    public RefreshToken generateRefreshToken(User user) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiredAt = new Date(issuedAt.getTime()+refreshTokenLifetime.toMillis());
        Map<String, Object> claims = Map.of("role", user.getRole());
        String token = buildToken(claims, UserDetailsImpl.of(user), issuedAt, expiredAt);
        return RefreshToken.builder()
                .user(user)
                .issuedAt(issuedAt)
                .expiredAt(expiredAt)
                .token(token)
                .build();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String buildToken(Map<String,Object> claims,
                              UserDetails userDetails,
                              Date issuedAt,
                              Date expiredAt) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaimsFromToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
