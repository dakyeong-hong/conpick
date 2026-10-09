package com.team1.conpick.youtube;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class YoutubeTestController {

    private final YoutubeService youtubeService;

    @GetMapping("/test/youtube-url")
    public String test(){
        return youtubeService.createAuthorizationUrl(1L);
    }
}
