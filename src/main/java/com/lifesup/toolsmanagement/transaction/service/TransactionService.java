package com.lifesup.toolsmanagement.transaction.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.transaction.dto.TransactionDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
import com.lifesup.toolsmanagement.user.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService extends GenericService<Transaction, TransactionDTO, UUID> {
    TransactionDTO updateTransaction(UUID transactionId, TransactionDTO transactionDTO);

    List<Transaction> getTransactionByUserId(UUID userId);

    String createTransaction(int year, int amountDevice);

    String checkTransaction(User user, String token, BigDecimal amountMoney);
}

