package com.lifesup.toolsmanagement.security.service;

import com.lifesup.toolsmanagement.security.dto.SignInRequestDTO;
import com.lifesup.toolsmanagement.security.dto.SignInResponseDTO;
import com.lifesup.toolsmanagement.security.dto.SignUpRequestDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
import com.lifesup.toolsmanagement.transaction.service.TransactionService;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.model.User;
import com.lifesup.toolsmanagement.user.service.MapUserDeviceService;
import com.lifesup.toolsmanagement.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
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
        userService.save(user);
        return "Sign Up Success";
    }

    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getUsername(),
                signInRequestDTO.getPassword()
        ));
        User user = userService.getByUsername(signInRequestDTO.getUsername());
        List<Transaction> transactionList = transactionService.getTransactionByUserId(user.getId());
        String jwtToken = jwtService.generateToken(user);
        String message = "";
        if (transactionList.isEmpty()) {
            MapUserDevice mapUserDevice = new MapUserDevice();
            mapUserDevice.setUser(user);
            mapUserDevice.setDeviceId(signInRequestDTO.getDeviceId());
            mapUserDevice.setExpDate(null);
            mapUserDevice.setTransaction(null);
            mapUserDeviceService.save(mapUserDevice);
            message = "Login success. The account has not purchased the tool or the tool has expired";
        } else {
            outer:
            {
                for (Transaction transaction : transactionList) {
                    List<MapUserDevice> mapUserDevices = mapUserDeviceService
                            .getMapUserDeviceByUserIdAndTransactionId(transaction.getUser().getId(), transaction.getId());
                    for (MapUserDevice mapUserDevice : mapUserDevices) {
                        if (signInRequestDTO.getDeviceId().equals(mapUserDevice.getDeviceId())) {
                            mapUserDevice.setDeleted(false);
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String formattedDate = dateTimeFormatter.format(mapUserDevice.getExpDate());
                            message = String.format("Login success. This device can be used tool until: %s", formattedDate);
                            break outer;
                        }
                    }
                    if (transaction.getAmountDevice() == mapUserDevices.size()) {
                        message = "Login success. But device is is full, this device can't use tool";
                    } else if (transaction.getAmountDevice() > mapUserDevices.size()) {
                        MapUserDevice mapUserDevice = new MapUserDevice();
                        mapUserDevice.setDeviceId(signInRequestDTO.getDeviceId());
                        mapUserDevice.setExpDate(transaction.getExpDate());
                        mapUserDevice.setUser(user);
                        mapUserDevice.setTransaction(transaction);
                        mapUserDeviceService.save(mapUserDevice);
                        mapUserDevices.add(mapUserDevice);
                        message = String.format("Login success. This account just added a new device (%d/%d)", mapUserDevices.size(), transaction.getAmountDevice());
                        break outer;
                    }
                }
            }
        }
        return SignInResponseDTO.builder()
                .message(message)
                .token(jwtToken)
                .build();
    }
}