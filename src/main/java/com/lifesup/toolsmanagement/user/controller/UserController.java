package com.lifesup.toolsmanagement.user.controller;

import com.lifesup.toolsmanagement.common.model.ResponseDTO;
import com.lifesup.toolsmanagement.common.util.ResponseUtils;
import com.lifesup.toolsmanagement.user.dto.UserDTO;
import com.lifesup.toolsmanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all-user")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        return ResponseUtils.get(userService.findAllDto(UserDTO.class), HttpStatus.OK);
    }
}
