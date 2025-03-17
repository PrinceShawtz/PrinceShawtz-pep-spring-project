package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

/**
 * The `SocialMediaController` class is a controller class in a Spring application that handles HTTP
 * requests related to social media functionalities. It contains various methods annotated with
 * `@PostMapping`, `@GetMapping`, `@DeleteMapping`, and `@PatchMapping` that define endpoints for
 * registering accounts, logging in, creating messages, retrieving messages, deleting messages,
 * updating message text, and getting messages by account ID. The class uses services like
 * `AccountService` and `MessageService` to perform the necessary operations for each endpoint.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    /**
     * Constructs a new `SocialMediaController` with the specified `AccountService` and
     * `MessageService`.
     *
     * @param accountService the service for account-related operations
     * @param messageService the service for message-related operations
     */
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Registers a new account with the specified account details. The account details are provided
     * in the request body as a JSON object. The method returns a response entity containing the
     * registered account if the registration is successful. If the registration fails due to
     * invalid account details or if an account with the same username already exists, the method
     * returns an appropriate response entity with an error status code.
     *
     * @param account the account details for the new account
     * @return a response entity containing the registered account or an error message
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
        return accountService.registerAccount(account);
    }

    /**
     * Logs in an account with the specified username and password. The username and password are
     * provided in the request body as a JSON object. The method returns a response entity containing
     * the authenticated account if the login is successful. If the login fails due to invalid
     * credentials, the method returns an appropriate response entity with an error status code.
     *
     * @param account the account details for logging in
     * @return a response entity containing the authenticated account or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Account account) {
        try {
            ResponseEntity<Account> response = accountService.login(account);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Creates a new message with the specified message details. The message details are provided in
     * the request body as a JSON object. The method returns a response entity containing the created
     * message if the creation is successful. If the creation fails due to invalid message details or
     * an invalid account ID, the method returns an appropriate response entity with an error status
     * code.
     *
     * @param message the message details for the new message
     * @return a response entity containing the created message or an error message
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    /**
     * Retrieves all messages from the database. The method returns a response entity containing a
     * list of all messages if the retrieval is successful.
     *
     * @return a response entity containing a list of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    /**
     * Retrieves a message with the specified message ID. The message ID is provided as a path
     * variable in the URL. The method returns a response entity containing the message with the
     * specified ID if the retrieval is successful. If the message with the specified ID does not
     * exist, the method returns an appropriate response entity with an error status code.
     *
     * @param messageId the ID of the message to retrieve
     * @return a response entity containing the message with the specified ID or an error message
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId);
    }

    /**
     * Deletes a message with the specified message ID. The message ID is provided as a path variable
     * in the URL. The method returns a response entity with a success status code if the deletion is
     * successful. If the message with the specified ID does not exist, the method returns an
     * appropriate response entity with an error status code.
     *
     * @param messageId the ID of the message to delete
     * @return a response entity with a success status code or an error message
     */
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Integer messageId) {
        return messageService.deleteMessageById(messageId);
    }

    /**
     * Updates the text of a message with the specified message ID. The message ID and the new message
     * text are provided as path variables and in the request body, respectively. The method returns
     * a response entity containing the updated message if the update is successful. If the message
     * with the specified ID does not exist, the method returns an appropriate response entity with
     * an error status code.
     *
     * @param messageId the ID of the message to update
     * @param newMessageText the new text for the message
     * @return a response entity containing the updated message or an error message
     */
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Object> updateMessageText(@PathVariable Integer messageId, @RequestBody Message newMessageText) {
        return messageService.updateMessageText(messageId, newMessageText);
    }

    /**
     * Retrieves all messages posted by an account with the specified account ID. The account ID is
     * provided as a path variable in the URL. The method returns a response entity containing a list
     * of messages posted by the account with the specified ID if the retrieval is successful.
     *
     * @param accountId the ID of the account to retrieve messages for
     * @return a response entity containing a list of messages posted by the account with the specified ID
     */
    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}