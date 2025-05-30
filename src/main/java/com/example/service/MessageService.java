package com.example.service;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message addNewMessage(Message newMessage){
        if(newMessage.getMessageText() == null || newMessage.getMessageText().isBlank() || newMessage.getMessageText().trim().length() > 255){
            throw new IllegalArgumentException("Invalid message text");
        }
        if(newMessage.getPostedBy() == null || !accountRepository.existsById(newMessage.getPostedBy())){
            throw new IllegalArgumentException("Invalid user");
        }

        return messageRepository.save(newMessage);
    }

    public List<Message> retrieveMessages(){
        return messageRepository.findAll();
    }

    public Message retrieveMessageById(int messageId){
        Optional<Message> message = messageRepository.findById(messageId);
        return message.orElse(null);
    }

    public boolean deleteMessageById(Integer messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public boolean updateMessageById(Message updatedMessage, Integer messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        
        if (optionalMessage.isEmpty()){
            throw new ResourceNotFoundException("Message not found");
        }
        if (updatedMessage.getMessageText().isBlank() || updatedMessage.getMessageText().trim().length() > 255){
            throw new IllegalArgumentException("Invalid message text");
        }
       
        if(optionalMessage.isPresent()){
            Message message = optionalMessage.get();
            message.setMessageText(updatedMessage.getMessageText());
            messageRepository.save(message);
            return true;
        }

        return false;
    }

    public List<Message> retrieveMessagesByAccountId(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
