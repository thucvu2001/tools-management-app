package com.lifesup.toolsmanagement.transaction.service;

import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.transaction.dto.TransactionDTO;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
import com.lifesup.toolsmanagement.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final ModelMapper modelMapper;


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
        transaction.setDeleted(transaction.isDeleted());
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Override
    public List<TransactionDTO> getTransactionByUserId(UUID userId) {
        List<Transaction> transactions = transactionRepository.findByUser_Id(userId);
        return transactions.stream().map(transaction -> mapper.map(transaction, TransactionDTO.class)).toList();
    }
}
