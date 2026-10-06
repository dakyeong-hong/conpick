package com.team1.conpick.user.jwt;

import com.team1.conpick.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;

@Component
public class JwtProvider {

    //SecretKey : 암호화 키를 담는 자바 표준 인터페이스.
    private final SecretKey key;
    private final long expirationMs;

    public JwtProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String createToken(User user){
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();

    }

    public boolean validateToken(String token){
        try{
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            return false; //위조, 만료, 형식 오루 모두 여기로
        }
    }

    public Long getUserId(String token){
        return Long.valueOf(parseClaims(token).getSubject());
    }

    public String getRole(String token){
        return parseClaims(token).get("role", String.class);
    }


    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(key) //발급할 때 쓴 것과 같은 키로 서명 확인
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
