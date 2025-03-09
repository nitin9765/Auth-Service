package com.edigest.authservice.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserKafkaDto {
    private String name;
    private String phoneNumber;
    private String email;
}
