package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<Account> registerAccount(Account account) {
        // Check if the provided username is not blank and password is at least 4 characters long
        if (account.getUsername() == null || account.getUsername().isBlank() || 
            account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body(null);
        }
    
        if (doesUsernameExist(account.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }
    
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
    }

    public boolean doesUsernameExist(String username) {
        return accountRepository.existsByUsername(username);
    }

    public ResponseEntity<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Account authenticatedAccount = accountRepository.findByUsernameAndPassword(username, password);
        if (authenticatedAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(authenticatedAccount);
    }
}