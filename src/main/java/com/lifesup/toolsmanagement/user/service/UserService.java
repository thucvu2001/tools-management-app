package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.user.dto.UserDTO;
import com.lifesup.toolsmanagement.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends GenericService<User, UserDTO, Integer>, UserDetailsService {
    User getByUsername(String username);
}

