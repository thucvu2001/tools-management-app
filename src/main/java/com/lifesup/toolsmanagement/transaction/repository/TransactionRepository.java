package com.lifesup.toolsmanagement.transaction.repository;

import com.lifesup.toolsmanagement.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(nativeQuery = true,
            value = """
                    select *
                    from transactions as t
                    where t.user_id = ?1 and t.is_active = true and t.exp_date >= ?2
                    """
    )
    List<Transaction> findByUser_Id(UUID userId, LocalDate currDate);
}
