package com.lifesup.toolsmanagement.transaction.model;

import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private UUID id;
    private BigDecimal value;
    private LocalDate expDate;
    private int amountDevice;
    private String token;
    boolean isActive;
    private LocalDate createdDate = LocalDate.now();
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Set<MapUserDevice> mapUserDeviceSet = new LinkedHashSet<>();
}
