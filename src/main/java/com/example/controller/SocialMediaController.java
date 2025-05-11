package com.example.controller;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;

/**
 * Handles API endpoints for user registration, login, message creation, retrieval, update, and deletion.
 * Utilizes Spring's @RestController for handling incoming web requests and @RequestMapping for mapping
 * specific URL paths to handler methods. Leverages @PostMapping, @GetMapping, @DeleteMapping, and
 * @PatchMapping for different HTTP methods. Extracts path variables using @PathVariable and request
 * body data using @RequestBody. Returns responses with appropriate HTTP status codes using
 * ResponseEntity. Autowires AccountService and MessageService to handle the business logic.
 */
@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount){
        Account account = accountService.register(newAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(account);
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account loginAccount) throws AuthenticationException {
        Account account = accountService.login(loginAccount);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message newMessage){
        Message message = messageService.addNewMessage(newMessage);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> retrieveMessages(){
        List<Message> messages = messageService.retrieveMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> retrieveMessageById(@PathVariable Integer messageId){
        Message message = messageService.retrieveMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        boolean deleted = messageService.deleteMessageById(messageId);
        if (deleted){
            return ResponseEntity.ok(1); // number of rows deleted
        } else {
            return ResponseEntity.ok().build(); // empty body
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@RequestBody Message updatedMessage, @PathVariable Integer messageId){
        boolean updated = messageService.updateMessageById(updatedMessage, messageId);
        if(updated){
            return ResponseEntity.ok(1); // number of rows updated
        } else {
            return ResponseEntity.ok().build(); // empty body
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> retrieveMessagesByAccountId(@PathVariable Integer accountId){
        List<Message> messages = messageService.retrieveMessagesByAccountId(accountId);
        
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
