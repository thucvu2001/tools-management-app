package com.lifesup.toolsmanagement.transaction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private BigDecimal value;
    private LocalDate expDate;
    private int amountDevice;
    private String token;
    boolean isActive;
    private LocalDate createdDate;
    private boolean isDeleted;
}
