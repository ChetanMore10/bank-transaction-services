package com.banktransaction.serviceImpl;

import com.banktransaction.entity.Account;
import com.banktransaction.entity.Transaction;
import com.banktransaction.repository.AccountRepo;
import com.banktransaction.repository.TransactionRepo;
import com.banktransaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    // Transaction related database operations साठी repository
    @Autowired
    private TransactionRepo transactionRepo;

    // Account related database operations साठी repository
    @Autowired
    private AccountRepo accountRepo;

    // Transaction amount valid आहे का ते check करण्यासाठी method
    private void validateAmount(BigDecimal amount) {

        // Amount null किंवा zero/negative असेल तर transaction allow करणार नाही
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transaction Amount must be Greater than Zero");
        }
    }

    // प्रत्येक transaction साठी unique transaction reference तयार करतो
    private String generateReference() {
        return "TXN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    // DEPOSIT
    @Override
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        // Transaction amount valid आहे का check करतो
        validateAmount(amount);
        // दिलेल्या accountId वरून account शोधतो
        Account account = accountRepo.findById(accountId)
                // Account मिळाला नाही तर exception throw करतो
                .orElseThrow(() ->
                        new RuntimeException("Account not found with id: " + accountId));

        // Account active आहे का ते check करतो
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        // Existing balance मध्ये deposit amount add करतो
        account.setBalance(account.getBalance().add(amount));

        // Updated account database मध्ये save करतो
        accountRepo.save(account);

        // नवीन transaction object तयार करतो
        Transaction transaction = new Transaction();

        // Unique transaction reference तयार करतो
        transaction.setTransactionReference(generateReference());

        // Transaction type DEPOSIT set करतो
        transaction.setTransactionType("DEPOSIT");

        // Deposited amount set करतो
        transaction.setAmount(amount);

        // Deposit कोणत्या account मध्ये झाला ते set करतो
        transaction.setToAccount(account);

        // Transaction successful आहे असे status set करतो
        transaction.setStatus("SUCCESS");

        // Transaction description set करतो
        transaction.setDescription("Amount Deposit Successfully..!");

        // Current date आणि time set करतो
        transaction.setTransactionDate(LocalDateTime.now());

        // Transaction database मध्ये save करतो
        return transactionRepo.save(transaction);
    }

    // WITHDRAW
    @Override
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {

        // Amount valid आहे का check करतो
        validateAmount(amount);

        // Account database मधून शोधतो
        Account account = accountRepo.findById(accountId)
                // Account सापडला नाही तर exception
                .orElseThrow(() ->
                        new RuntimeException("Account not found with id: " + accountId));

        // Account active आहे का check करतो
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("Account is not Active");
        }

        // Account मध्ये पुरेसा balance आहे का check करतो
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Withdrawal amount account balance मधून subtract करतो
        account.setBalance(account.getBalance().subtract(amount));

        // Updated balance database मध्ये save करतो
        accountRepo.save(account);

        // Withdrawal transaction object तयार करतो
        Transaction transaction = new Transaction();

        // Unique transaction reference तयार करतो
        transaction.setTransactionReference(generateReference());

        // Transaction type WITHDRAWAL set करतो
        transaction.setTransactionType("WITHDRAWAL");

        // Withdrawal amount set करतो
        transaction.setAmount(amount);

        // कोणत्या account मधून पैसे काढले ते set करतो
        transaction.setFromAccount(account);

        // Transaction successful आहे
        transaction.setStatus("SUCCESS");

        // Description set करतो
        transaction.setDescription("Account withdrawal Successfully..!");

        // Transaction date/time set करतो
        transaction.setTransactionDate(LocalDateTime.now());

        // Transaction database मध्ये save करतो
        return transactionRepo.save(transaction);
    }

    // FUND TRANSFER
    @Override
    @Transactional
    public Transaction transfer(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount) {

        // Amount valid आहे का check करतो
        validateAmount(amount);

        // Sender आणि receiver account same नसावेत
        if (fromAccountId.equals(toAccountId)) {
            throw new RuntimeException("Sender and receiver accounts cannot be same");
        }

        // Sender account शोधतो
        Account fromAccount = accountRepo.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Sender account not found with id : " + fromAccountId));

        // Receiver account शोधतो
        Account toAccount = accountRepo.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Receiver account not found with id : " + toAccountId));

        // Sender account active आहे का check करतो
        if (!"ACTIVE".equalsIgnoreCase(fromAccount.getStatus())) {
            throw new RuntimeException("Sender account is not active");
        }

        // Receiver account active आहे का check करतो
        if (!"ACTIVE".equalsIgnoreCase(toAccount.getStatus())) {
            throw new RuntimeException("Receiver account is not active");
        }

        // Sender कडे sufficient balance आहे का check करतो
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Sender account मधून amount subtract करतो
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));

        // Receiver account मध्ये amount add करतो
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // Updated sender account save करतो
        accountRepo.save(fromAccount);

        // Updated receiver account save करतो
        accountRepo.save(toAccount);

        // Transfer transaction तयार करतो
        Transaction transaction = new Transaction();

        // Unique transaction reference तयार करतो
        transaction.setTransactionReference(generateReference());

        // Transaction type TRANSFER set करतो
        transaction.setTransactionType("TRANSFER");

        // Transfer amount set करतो
        transaction.setAmount(amount);

        // Sender account set करतो
        transaction.setFromAccount(fromAccount);

        // Receiver account set करतो
        transaction.setToAccount(toAccount);

        // Transaction successful आहे
        transaction.setStatus("SUCCESS");

        // Description set करतो
        transaction.setDescription("Fund transfer Successfully..!");

        // Transaction date/time set करतो
        transaction.setTransactionDate(LocalDateTime.now());

        // Transaction database मध्ये save करतो
        return transactionRepo.save(transaction);
    }

    // GET ALL TRANSACTIONS
    @Override
    public List<Transaction> getAllTransaction() {
        // Database मधून सर्व transactions आणतो
        return transactionRepo.findAll();
    }

    // GET TRANSACTION BY ID
    @Override
    public Transaction getTransactionById(Long id) {
        // ID वरून transaction शोधतो
        return transactionRepo.findById(id)
                // Transaction सापडला नाही तर exception
                .orElseThrow(() -> new RuntimeException("Transaction not found with id : " + id));
    }

    // GET TRANSACTIONS BY ACCOUNT
    @Override
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        // सर्व transactions database मधून आणतो
        return transactionRepo.findAll()

                // Stream वापरून transactions filter करतो
                .stream()
                .filter(transaction ->
                        // Account sender असेल तर transaction select करतो
                        (transaction.getFromAccount() != null &&
                                transaction.getFromAccount().getId().equals(accountId))
                        ||
                                // Account receiver असेल तरी transaction select करतो
                                (transaction.getToAccount() != null &&
                                        transaction.getToAccount().getId().equals(accountId)))

                // Filter झालेल्या transactions ची List तयार करतो
                .toList();
    }
}