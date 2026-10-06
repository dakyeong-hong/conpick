package com.team1.conpick.auth;


import com.team1.conpick.user.User;
import com.team1.conpick.auth.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


//google 인증이 끝나면 Spring이 이 핸들러를 호출함.
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    // Google 인증이 성공하면 Spring Security가 이 메서드를 호출하면서 세 개를 넘겨줌. request, response, authentication.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        //OAuth2User : Google이 알려준 사용자 정보를 담은 객체.
        //Spring Security가 Google에서 받아온 정보를 여기에 담아서 authentication 안에 넣어줌.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        User user = authService.loginOrSignUp(oAuth2User);
        String token = jwtProvider.createToken(user);

        response.sendRedirect(redirectUri + "?token=" + token);
    }
}
