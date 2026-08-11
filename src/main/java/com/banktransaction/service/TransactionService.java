package com.banktransaction.service;

import com.banktransaction.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface TransactionService{

    Transaction deposit(Long accountId, BigDecimal amount);
    Transaction withdraw(Long accountId, BigDecimal amount);
    Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    List<Transaction> getAllTransaction();
    Transaction getTransactionById(Long id);
    List<Transaction> getTransactionsByAccount(Long accountId);
}
