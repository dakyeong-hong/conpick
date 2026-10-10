package com.team1.conpick.youtube;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class YoutubeAccountService {

    final private YoutubeAccountRepository youtubeAccountRepository;

    public YoutubeAccountService(YoutubeAccountRepository youtubeAccountRepository) {
        this.youtubeAccountRepository = youtubeAccountRepository;
    }

    /**
     * GET /auth/youtube/callback?code=abc123&state=xyz789
     * 이 요청은 Google이 브라우저를 우리 서버로 리다이렉트 시킨 거라 JWT 헤더가 없음.
     * 서버 입장에서는 code를 받았는데 어느 유저의 요청인지 알 수가 없음.
     */

    //state: 이 OAuth 요청을 시작한 유저가 누구인지 기억하는 일회용 번호표 / 5-10분, 한번 쓰고 삭제 -> Redis 이용.
    //랜덤 state 생성
    public String createYoutubeState(){
        return UUID.randomUUID().toString();
    }






}
