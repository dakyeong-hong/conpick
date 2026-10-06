package com.team1.conpick.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

//GoogleOAuth 사용
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String profileImageUrl;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Provider provider; //Google
    private String providerId; //로그인 시 식별자로 사용.

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Role role;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public User(String name, String email, Provider provider, String providerId, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.role = Role.USER;
    }

    public void updateProfile(String name, String profileImageUrl){
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }




}
