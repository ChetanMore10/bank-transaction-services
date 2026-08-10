package com.banktransaction.controller;

import com.banktransaction.entity.Account;
import com.banktransaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> postAccount(@RequestBody Account account){
        Account saveAccount = accountService.createAccount(account);
        return new ResponseEntity<>(saveAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAll (){
        List<Account> getAccounts = accountService.getAllAccount();
        return new ResponseEntity<>(getAccounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id){
        Account getAccount = accountService.getAccountById(id);
        return new ResponseEntity<>(getAccount, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account){
        Account updatedAccount = accountService.updateAccount(id,account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Account Deleted Successfully...!", HttpStatus.OK);
    }
}
