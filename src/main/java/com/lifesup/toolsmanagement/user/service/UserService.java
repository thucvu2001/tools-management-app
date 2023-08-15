package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.user.dto.UserDTO;
import com.lifesup.toolsmanagement.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends GenericService<User, UserDTO, UUID>, UserDetailsService {
    UserDTO updateUser(UUID userId, UserDTO userDTO);
}

