package com.banktransaction.controller;

import com.banktransaction.entity.Transaction;
import com.banktransaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Deposit money into an account
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount){
        Transaction transaction = transactionService.deposit(accountId,amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // Withdraw money from an account
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount){
        Transaction transaction = transactionService.withdraw(accountId,amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // Transfer money between two accounts
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount){

        Transaction transaction = transactionService.transfer(fromAccountId,toAccountId,amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transaction = transactionService.getAllTransaction();
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // Get transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@RequestParam Long id){
        Transaction transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // Get transaction history for an account
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@RequestParam Long accountId){
        List<Transaction> transaction = transactionService.getTransactionsByAccount(accountId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
