package com.edigest.authservice.service;

import com.edigest.authservice.entity.RefreshToken;
import com.edigest.authservice.entity.User;
import com.edigest.authservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;

    public RefreshToken createRefreshToken(String username) throws UsernameNotFoundException{
        User user = userService.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60 * 60 * 1000))
                .build();
        return refreshTokenRepository.save(token);
    }

    public RefreshToken isExpired(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.deleteById(token.getId());
            throw new RuntimeException("Invalid Refresh Token");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
