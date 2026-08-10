package com.banktransaction.service;

import com.banktransaction.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    Account createAccount(Account account);
    List<Account> getAllAccount();
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);
}