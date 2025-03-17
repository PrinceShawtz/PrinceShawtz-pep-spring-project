package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * The `MessageService` class is a service class in a Spring application that handles message-related
 * operations. It contains methods for creating messages, retrieving messages, deleting messages,
 * updating message text, and getting messages by account ID. The class uses an `MessageRepository`
 * to interact with the database.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    /**
     * Constructs a new `MessageService` with the specified `MessageRepository` and `AccountRepository`.
     *
     * @param messageRepository the repository for message-related operations
     * @param accountRepository the repository for account-related operations
     */
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new message with the specified message details. The message details are provided
     * in the request body as a JSON object. The method returns a response entity containing the
     * created message if the creation is successful. If the creation fails due to invalid message
     * details or if the account with the specified username does not exist, the method returns a
     * response entity with status code 400.
     *
     * @param message the message details to create
     * @return a response entity containing the created message if successful, or an error response
     */
    public ResponseEntity<Message> createMessage(Message message) {
        // Validate message details
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() ||
                message.getMessageText().length() > 255 || message.getPostedBy() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Check if the account with the specified username exists
        String username = getUsernameByAccountId(message.getPostedBy());
        if (username == null || !accountRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Save the message and return the saved message
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    /**
     * Retrieves the username associated with the specified account ID.
     *
     * @param accountId the account ID to retrieve the username for
     * @return the username associated with the account ID, or null if the account is not found
     */
    public String getUsernameByAccountId(Integer accountId) {
        // Find the account by account ID
        Account account = accountRepository.findByAccountId(accountId);

        // Return the username if the account is found, otherwise return null
        return account != null ? account.getUsername() : null;
    }

    /**
     * Retrieves all messages from the database.
     *
     * @return a list of all messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a message with the specified message ID.
     *
     * @param messageId the ID of the message to retrieve
     * @return the message with the specified ID, or null if the message is not found
     */
    public ResponseEntity<Message> getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            return ResponseEntity.ok(optionalMessage.get());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Deletes a message with the specified message ID.
     *
     * @param messageId the ID of the message to delete
     * @return a response entity indicating the success of the deletion
     */
    public ResponseEntity<Object> deleteMessageById(Integer messageId) {
        // Check if the message exists and delete it
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok().body("1");
        } else {
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Updates the text of a message with the specified message ID.
     *
     * @param messageId the ID of the message to update
     * @param newMessageText the new text for the message
     * @return a response entity indicating the success of the update
     */
    public ResponseEntity<Object> updateMessageText(Integer messageId, Message newMessageText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
    
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message not found");
        }
    
        Message message = optionalMessage.get();
    
        // Check if newMessageText is null or empty
        if (newMessageText.getMessageText() == null || newMessageText.getMessageText().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Message text cannot be empty");
        }
    
        // Check if newMessageText is too long
        if (newMessageText.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Message too long: it must have a length of at most 255 characters");
        }
    
        // Update the message text
        message.setMessageText(newMessageText.getMessageText());
        messageRepository.save(message);
    
        // Return the updated message
        return ResponseEntity.ok().body("1"); // Assuming "1" represents the number of rows updated
    }

    /**
     * Retrieves all messages posted by the account with the specified account ID.
     *
     * @param accountId the ID of the account to retrieve messages for
     * @return a list of messages posted by the account
     */
    public ResponseEntity<List<Message>> getMessagesByAccountId(Integer accountId) {
        List<Message> messages = messageRepository.findByPostedBy(accountId);

        return ResponseEntity.ok(messages);
    }
}