package com.edigest.authservice.service;

import com.edigest.authservice.entity.User;
import com.edigest.authservice.events.EventProducer;
import com.edigest.authservice.model.UserDto;
import com.edigest.authservice.model.UserKafkaDto;
import com.edigest.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventProducer eventProducer;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=findByUsername(username);
        if(user==null) throw new UsernameNotFoundException("user not found");
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getUserRoles().stream().toString())
                .build();
    }

    public Boolean alreadyExists(String username){
        return userRepository.findByUsername(username) != null;
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User signUpUser(UserDto user) throws ExecutionException, InterruptedException {
        try{
            if (Boolean.TRUE.equals(alreadyExists(user.getUsername()))) {
                return null;
            }
            User newUser = User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .userRoles(new HashSet<>())
                    .build();
            newUser=userRepository.save(newUser);
            // sending kafka event
            UserKafkaDto userKafkaDto = UserKafkaDto.builder()
                    .userId(newUser.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber()).build();
            eventProducer.publishToUserService("userService", userKafkaDto);
            return newUser;
        }catch (Exception e){
            log.error("Exception found in signUpUser user service method, error is: {}", e.getMessage());
            throw e;
        }
    }


}
