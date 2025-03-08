package com.edigest.authservice.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto{
    private String username;
    private String password;
    private String lastName;
    private Long phoneNumber;
    private String email;
}
