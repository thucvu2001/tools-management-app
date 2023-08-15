package com.lifesup.toolsmanagement.transaction.repository;

import com.lifesup.toolsmanagement.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUser_Id(UUID userId);
}