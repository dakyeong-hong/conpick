package com.team1.conpick.auth.jwt;


import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RefreshTokenCookieFactory {

    private static final String NAME = "refresh_token";

    public ResponseCookie create(String refreshToken){
        return base(refreshToken).maxAge(Duration.ofDays(14)).build();
    }

    public ResponseCookie delete(){
        return base("").maxAge(0).build();
    }

    public ResponseCookie.ResponseCookieBuilder base(String value){

        return ResponseCookie.from(NAME, value)
                .httpOnly(true) //JavaScript가 직접 다루지 않도록 설정.
                .secure(false) //HTTP에서도 쿠키를 보낼 수 있게(개발 환경) -> 운영환경에서는 true
                .sameSite("Lax") //CSRF 공격 등을 줄이기 위한 쿠키 보안 설정
                .path("/auth"); //쿠키 전송 범위 설정.
    }
}
