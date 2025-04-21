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
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
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
    public ResponseEntity<Message> retrieveMessageById(@PathVariable int messageId){
        Message message = messageService.retrieveMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
