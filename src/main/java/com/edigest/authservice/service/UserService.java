package com.edigest.authservice.service;

import com.edigest.authservice.entity.User;
import com.edigest.authservice.model.UserDto;
import com.edigest.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

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

    public User signUpUser(UserDto user){
        if(Boolean.TRUE.equals(alreadyExists(user.getUsername()))){
            return null;
        }
        String userId=UUID.randomUUID().toString();
        User newUser=User.builder()
                .userId(userId)
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .userRoles(new HashSet<>())
                .build();
        userRepository.save(newUser);
        return newUser;
    }


}
