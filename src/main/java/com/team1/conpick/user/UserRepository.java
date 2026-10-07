package com.team1.conpick.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    //provider + providerId는 유니크 제약 : 최대 1명. Optional로 받기.
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
    Optional<User> findByEmail(String email);

}
