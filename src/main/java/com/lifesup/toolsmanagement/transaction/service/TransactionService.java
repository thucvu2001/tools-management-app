package com.lifesup.toolsmanagement.transaction.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.transaction.dto.TransactionDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService extends GenericService<Transaction, TransactionDTO, UUID> {
    TransactionDTO updateTransaction(UUID transactionId, TransactionDTO transactionDTO);

    List<Transaction> getTransactionByUserId(UUID userId);
}

