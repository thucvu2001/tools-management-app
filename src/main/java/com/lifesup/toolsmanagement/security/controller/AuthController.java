package com.lifesup.toolsmanagement.security.controller;

import com.lifesup.toolsmanagement.common.model.ResponseDTO;
import com.lifesup.toolsmanagement.common.util.ResponseUtils;
import com.lifesup.toolsmanagement.security.dto.SignInRequestDTO;
import com.lifesup.toolsmanagement.security.dto.SignUpRequestDTO;
import com.lifesup.toolsmanagement.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;


    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return ResponseUtils.get(authenticationService.signUp(signUpRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseUtils.get(authenticationService.signIn(signInRequestDTO), HttpStatus.OK);
    }
}
