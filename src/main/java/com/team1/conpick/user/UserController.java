package com.team1.conpick.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    //test를 위한 임시 접근
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    public String me(@AuthenticationPrincipal Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        return user.getEmail();
    }
}
