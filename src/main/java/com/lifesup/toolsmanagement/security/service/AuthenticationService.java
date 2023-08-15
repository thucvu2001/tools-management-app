package com.lifesup.toolsmanagement.security.service;

import com.lifesup.toolsmanagement.security.dto.SignInRequestDTO;
import com.lifesup.toolsmanagement.security.dto.SignInResponseDTO;
import com.lifesup.toolsmanagement.security.dto.SignUpRequestDTO;
import com.lifesup.toolsmanagement.transaction.dto.TransactionDTO;
import com.lifesup.toolsmanagement.transaction.service.TransactionService;
import com.lifesup.toolsmanagement.user.dto.MapUserDeviceDTO;
import com.lifesup.toolsmanagement.user.dto.UserDTO;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.model.User;
import com.lifesup.toolsmanagement.user.service.MapUserDeviceService;
import com.lifesup.toolsmanagement.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserServiceImpl userService;
    private final TransactionService transactionService;
    private final MapUserDeviceService mapUserDeviceService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public String signUp(SignUpRequestDTO signUpRequestDTO) {
        if (!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getRePassword())) {
            throw new RuntimeException("rePassword does not match");
        }

        User user = new User();
        user.setUsername(signUpRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
        user.setEmail(signUpRequestDTO.getEmail());
        user.setPhone(signUpRequestDTO.getPhone());

        userService.saveWithEntity(user, UserDTO.class);
        log.info("Create user success");
        return "Sign Up Success";
    }

    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getUsername(),
                signInRequestDTO.getPassword()
        ));
        UserDetails userDetails = userService.loadUserByUsername(signInRequestDTO.getUsername());
        User user = (User) userDetails;
        List<TransactionDTO> transactionList = transactionService.getTransactionByUserId(user.getId());
        String jwtToken = jwtService.generateToken(user);
        String message = "";
        if (transactionList.isEmpty()) {
            MapUserDevice mapUserDevice = new MapUserDevice();
            mapUserDevice.setDeviceId(signInRequestDTO.getDeviceId());
            mapUserDevice.setExpDate(null);
            mapUserDevice.setTransaction(null);
            mapUserDeviceService.saveWithEntity(mapUserDevice, MapUserDeviceDTO.class);
            message = "The account has not purchased the tool";
        } else {

        }

        return SignInResponseDTO.builder()
                .message(message)
                .token(jwtToken)
                .build();
    }
}
