package com.lifesup.toolsmanagement.user.model;

import com.lifesup.toolsmanagement.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "map_user_device")
public class MapUserDevice {
    @Id
    @GeneratedValue
    private UUID id;
    private String deviceId;
    private LocalDate expDate;
    private LocalDate createdDate = LocalDate.now();
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
