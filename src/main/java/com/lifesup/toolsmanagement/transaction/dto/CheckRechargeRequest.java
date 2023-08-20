package com.lifesup.toolsmanagement.transaction.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckRechargeRequest {
    private String token;
    private BigDecimal amountMoney;
}
