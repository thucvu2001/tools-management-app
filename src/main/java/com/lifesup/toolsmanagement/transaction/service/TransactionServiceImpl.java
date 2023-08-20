package com.lifesup.toolsmanagement.transaction.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.security.service.JWTService;
import com.lifesup.toolsmanagement.transaction.dto.TransactionDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
import com.lifesup.toolsmanagement.transaction.repository.TransactionRepository;
import com.lifesup.toolsmanagement.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private static final String SECRET_KEY = "c342dd5ff691b05ce02f7d0dd292a65eb4d89965";


    @Override
    public JpaRepository<Transaction, UUID> getRepository() {
        return this.transactionRepository;
    }

    @Override
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override
    public TransactionDTO updateTransaction(UUID transactionId, TransactionDTO transactionDTO) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            throw new RuntimeException("Transaction with ID: " + transactionId + " not found");
        }
        Transaction transaction = optionalTransaction.get();
        transaction.setValue(transactionDTO.getValue());
        transaction.setExpDate(transactionDTO.getExpDate());
        transaction.setAmountDevice(transactionDTO.getAmountDevice());
        transaction.setToken(transactionDTO.getToken());
        transaction.setActive(transactionDTO.isActive());
        transaction.setCreatedDate(transactionDTO.getCreatedDate());
        transaction.setDeleted(transactionDTO.isDeleted());
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public List<Transaction> getTransactionByUserId(UUID userId) {
        return transactionRepository.findByUser_Id(LocalDate.now(), userId);
    }

    @Override
    public String createTransaction(int year, int amountDevice) {
        BigDecimal value = BigDecimal.valueOf(50 * year * amountDevice);
        log.info(value.toString());
        return jwtService.generateToken(year, amountDevice, value);
    }

    @Override
    public String checkTransaction(User user, String token, BigDecimal amountMoney) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String valueString = decodedJWT.getClaim("value").asString();
        BigDecimal value = new BigDecimal(valueString);
        if (amountMoney.compareTo(value) < 0) {
            return "Not enough money";
        } else {
            int amountDevice = decodedJWT.getClaim("amountDevice").asInt();
            int year = decodedJWT.getClaim("year").asInt();
            Transaction transaction = Transaction.builder()
                    .amountDevice(amountDevice)
                    .createdDate(LocalDate.now())
                    .expDate(LocalDate.now().plusYears(year))
                    .isActive(true)
                    .isDeleted(false)
                    .token(token)
                    .value(value)
                    .user(user)
                    .build();
            transactionRepository.save(transaction);
            return "Recharge successful";
        }
    }
}
