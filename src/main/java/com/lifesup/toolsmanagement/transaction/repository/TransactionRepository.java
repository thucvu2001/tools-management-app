package com.lifesup.toolsmanagement.transaction.repository;

import com.lifesup.toolsmanagement.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = """
            SELECT * FROM transactions AS t
            WHERE t.exp_date > CURRENT_DATE
            AND t.is_active = true 
            AND t.user_id = :userId
            """, nativeQuery = true
    )
    List<Transaction> findByUserId(@Param("userId") Integer userId);

    @Query(nativeQuery = true, value = """
            SELECT * FROM transactions AS t
            WHERE t.created_date BETWEEN :startDate AND :endDate
            """)
    List<Transaction> findTransactionsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
