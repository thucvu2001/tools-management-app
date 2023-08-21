package com.lifesup.toolsmanagement.transaction.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lifesup.toolsmanagement.common.model.ResponseDTO;
import com.lifesup.toolsmanagement.common.util.ResponseUtils;
import com.lifesup.toolsmanagement.transaction.dto.CheckRechargeRequest;
import com.lifesup.toolsmanagement.transaction.service.TransactionService;
import com.lifesup.toolsmanagement.user.model.User;
import com.lifesup.toolsmanagement.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private static final String SECRET_KEY = "c342dd5ff691b05ce02f7d0dd292a65eb4d89965";

    @GetMapping("/recharge/{year}/{amount-device}")
    public ResponseEntity<ResponseDTO> rechargeMoney(@PathVariable("year") int year, @PathVariable("amount-device") int amountDevice) {
        if (year < 1 || amountDevice < 1) {
            throw new RuntimeException("The number can't be less than 1");
        }
        String token = transactionService.createTransaction(year, amountDevice);
        return ResponseUtils.get(token, HttpStatus.OK);
    }

    @PostMapping("/check-recharge")
    public ResponseEntity<ResponseDTO> checkRecharge(@RequestHeader(AUTHORIZATION) String authHeader, @RequestBody CheckRechargeRequest checkRechargeRequest) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            RuntimeException exception = new RuntimeException("User is not Authentication");
            return ResponseUtils.error(exception, HttpStatus.FORBIDDEN);
        }
        String authToken = authHeader.substring(7);
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(authToken);

        String username = decodedJWT.getSubject();
        User user = (User) userService.loadUserByUsername(username);

        String response = transactionService.checkTransaction(user, checkRechargeRequest.getToken(), checkRechargeRequest.getAmountMoney());
        if (response.equals("Not enough money")) {
            RuntimeException exception = new RuntimeException("Not enough money");
            return ResponseUtils.error(exception, HttpStatus.BAD_REQUEST);
        } else {
            return ResponseUtils.get(response, HttpStatus.OK);
        }
    }

    @PostMapping("check-expiration-date")
    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpirationDate() {
        transactionService.checkExpiration();
    }

    @PostMapping("export-transaction")
    @Scheduled(cron = "0 0 0 L * ?")
    public void exportTransaction() throws IOException {
        LocalDate localDate = LocalDate.now();
        LocalDate lastDateOfMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDateOfMoth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        transactionService.exportTransactionToExcel(firstDateOfMoth, lastDateOfMonth);
    }
}
