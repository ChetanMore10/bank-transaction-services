package com.banktransaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionReference;

    private String transactionType;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    @JsonIgnore
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    @JsonIgnore
    private Account toAccount;

    private String status;

    private String description;

    private LocalDateTime transactionDate;
}