package com.lifesup.toolsmanagement.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRequestDTO {
    private String username;
    private String password;
    private String deviceId;
}
