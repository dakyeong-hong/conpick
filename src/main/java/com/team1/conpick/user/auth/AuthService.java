package com.team1.conpick.user.auth;

import com.team1.conpick.user.Provider;
import com.team1.conpick.user.User;
import com.team1.conpick.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public User loginOrSignUp(OAuth2User oAuth2User){

        //1. Google이 준 정보 꺼내기
        String sub = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        //2. 기존 회원인지 조회
        Optional<User> found= userRepository.findByProviderAndProviderId(Provider.google, sub);

        //3-a. 기존 회원이면: 프로필 갱신 후 반환(로그인)
        if(found.isPresent()){
            User user = found.get();
            user.updateProfile(name, picture);
            return user;
        }

        //3-b. 없으면 : 새로 만들어 저장 (회원가입)
        User newUser = User.builder()
                .provider(Provider.google)
                .providerId(sub)
                .email(email)
                .name(name)
                .profileImageUrl(picture)
                .build();

        return userRepository.save(newUser);
    }
}
