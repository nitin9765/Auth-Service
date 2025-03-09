package com.edigest.authservice.controller;

import com.edigest.authservice.config.jwt.JwtUtil;
import com.edigest.authservice.entity.RefreshToken;
import com.edigest.authservice.entity.User;
import com.edigest.authservice.model.UserDto;
import com.edigest.authservice.request.AuthRequestDto;
import com.edigest.authservice.request.RefreshTokenRequestDto;
import com.edigest.authservice.response.JwtResponseDto;
import com.edigest.authservice.service.RefreshTokenService;
import com.edigest.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {
    //    "/auth/v1/login",
//            "/auth/v1/refreshToken",
//            "/auth/v1/signup"
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto user) {
        try {
            Boolean isSignedUp = userService.signUpUser(user) == null;
            if (Boolean.TRUE.equals(isSignedUp)) {
                return new ResponseEntity<>("Already Exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
            String jwtToken = jwtUtil.generateToken(user.getUsername());
            return new ResponseEntity<>(
                    JwtResponseDto.builder()
                            .accessToken(jwtToken)
                            .token(refreshToken.getToken())
                            .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception in signUp", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(),
                        authRequestDto.getPassword()
                ));
        if (authentication.isAuthenticated()) {
            User user=userService.findByUsername(authRequestDto.getUsername());
            RefreshToken refreshToken = refreshTokenService.findByUserId(user)
                    .orElseGet(() -> refreshTokenService.createRefreshToken(authRequestDto.getUsername()));
            return new ResponseEntity<>(
                    JwtResponseDto.builder().accessToken(
                                    jwtUtil.generateToken(authRequestDto.getUsername()))
                            .token(refreshToken.getToken())
                            .build(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to login", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/refreshToken")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        return refreshTokenService.findByToken(refreshTokenRequestDto.getToken())
                .map(refreshTokenService::isExpired)
                .map(RefreshToken::getUser)
                .map(userInfo-> {
                    String accessToken = jwtUtil.generateToken(userInfo.getUsername());
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDto.getToken())
                            .build();
                }).orElseThrow(()->new RuntimeException("Refresh Token not found in DB"));
    }

}
