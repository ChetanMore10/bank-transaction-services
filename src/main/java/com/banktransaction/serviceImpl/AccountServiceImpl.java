package com.banktransaction.serviceImpl;

import com.banktransaction.entity.Account;
import com.banktransaction.repository.AccountRepo;
import com.banktransaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public Account createAccount(Account account) {
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        if(account.getBalance() == null){
            account.setBalance(BigDecimal.ZERO);
        }

        if(account.getStatus() == null || account.getStatus().isBlank()){
            account.setStatus("ACTIVE");
        }

        return accountRepo.save(account);
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepo.findAll();
    }

    @Override
    public Account getAccountById(Long id) {

        return accountRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found with id :" + id));
    }

    @Override
    public Account updateAccount(Long id, Account account) {

        Account existingAccount = accountRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Account not found with id: " + id));

        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setStatus(account.getStatus());
        existingAccount.setUser(account.getUser());
        existingAccount.setUpdatedAt(LocalDateTime.now());

        return accountRepo.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Account not found with id :" + id));
        accountRepo.delete(account);
    }
}
