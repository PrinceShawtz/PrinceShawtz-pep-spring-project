package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

/**
 * The `AccountService` class is a service class in a Spring application that handles account-related
 * operations. It contains methods for registering an account, logging in, and checking if a username
 * already exists. The class uses an `AccountRepository` to interact with the database.
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * Constructs a new `AccountService` with the specified `AccountRepository`.
     *
     * @param accountRepository the repository for account-related operations
     */
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account with the specified account details. The account details are provided
     * in the request body as a JSON object. The method returns a response entity containing the
     * registered account if the registration is successful. If the registration fails due to
     * invalid account details or if an account with the same username already exists, the method
     * returns a response entity with status code 400 or 409, respectively.
     *
     * @param account the account details to register
     * @return a response entity containing the registered account if successful, or an error response
     */
    public ResponseEntity<Account> registerAccount(Account account) {
        // Validate account details
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

    /**
     * Checks if a username already exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean doesUsernameExist(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Logs in an account with the specified username and password. The username and password are
     * provided in the request body as a JSON object. The method returns a response entity containing
     * the authenticated account if the login is successful. If the login fails due to invalid
     * credentials, the method returns a response entity with status code 401.
     *
     * @param account the account details for logging in
     * @return a response entity containing the authenticated account if successful, or an error response
     */
    public ResponseEntity<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        // Validate username and password
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