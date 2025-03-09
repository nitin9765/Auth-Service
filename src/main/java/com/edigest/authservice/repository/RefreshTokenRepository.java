package com.edigest.authservice.repository;

import com.edigest.authservice.entity.RefreshToken;
import com.edigest.authservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    RefreshToken findByUser(User user);
    RefreshToken findByToken(String token);
}
