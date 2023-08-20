package com.lifesup.toolsmanagement.security.service;

import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.security.dto.SignInRequestDTO;
import com.lifesup.toolsmanagement.security.dto.SignInResponseDTO;
import com.lifesup.toolsmanagement.security.dto.SignUpRequestDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
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

import java.time.LocalDate;
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
    private final Mapper mapper;

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
        log.info("User ID:" + user.getId());
        return "Sign Up Success";
    }

    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getUsername(),
                signInRequestDTO.getPassword()
        ));
        UserDetails userDetails = userService.loadUserByUsername(signInRequestDTO.getUsername());
        User user = (User) userDetails;
        user.setDelete(false);
        userService.updateUser(user.getId(), mapper.map(user, UserDTO.class));
        System.out.println("User email: " + user.getEmail());

        List<Transaction> transactionList = transactionService.getTransactionByUserId(user.getId());
        System.out.println("transactionList: " + transactionList);
        String jwtToken = jwtService.generateToken(user);
        String message = "";
        if (transactionList.isEmpty()) {
            MapUserDevice mapUserDevice = new MapUserDevice();
            mapUserDevice.setUser(user);
            mapUserDevice.setDeviceId(signInRequestDTO.getDeviceId());
            mapUserDevice.setExpDate(null);
            mapUserDevice.setTransaction(null);
            mapUserDeviceService.saveWithEntity(mapUserDevice, MapUserDeviceDTO.class);
            message = "Login success. The account has not purchased the tool or the tool has expired";
        } else {
            outer:
            {
                for (Transaction transaction : transactionList) {
                    List<MapUserDevice> mapUserDevices = mapUserDeviceService
                            .getMapUserDeviceByUserIdAndTransactionId(transaction.getUser().getId(), transaction.getId());
                    System.out.println("mapUserDevices: " + mapUserDevices);
                    for (MapUserDevice mapUserDevice : mapUserDevices) {
                        if (signInRequestDTO.getDeviceId().equals(mapUserDevice.getDeviceId())) {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String formattedDate = dateTimeFormatter.format(mapUserDevice.getExpDate());
                            message = String.format("Login success. This device can be used until: %s", formattedDate);
                            break outer;
                        }
                    }
                    if (transaction.getAmountDevice() == mapUserDevices.size()) {
                        MapUserDevice mapUserDevice = new MapUserDevice();
                        mapUserDevice.builder()
                                .deviceId(signInRequestDTO.getDeviceId())
                                .expDate(null)
                                .transaction(null)
                                .user(user)
                                .build();
                        mapUserDeviceService.saveWithEntity(mapUserDevice, MapUserDeviceDTO.class);
                        message = "Login success. But device is is full, this device can't use tool";
                        break outer;
                    } else if (transaction.getAmountDevice() < mapUserDevices.size()) {
                        MapUserDevice mapUserDevice = new MapUserDevice();
                        mapUserDevice.builder()
                                .deviceId(signInRequestDTO.getDeviceId())
                                .expDate(transaction.getExpDate())
                                .createdDate(LocalDate.now())
                                .isDeleted(false)
                                .user(user)
                                .transaction(transaction)
                                .build();
                        mapUserDeviceService.saveWithEntity(mapUserDevice, MapUserDeviceDTO.class);
                        mapUserDevices.add(mapUserDevice);
                        message = String.format("Login success. This account just added a new device (%d/%d)", mapUserDevices.size() + 1, transaction.getId());
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
