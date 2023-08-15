package com.lifesup.toolsmanagement.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {
    private String username;
    private String password;
    private String rePassword;
    private String email;
    private String phone;
}
