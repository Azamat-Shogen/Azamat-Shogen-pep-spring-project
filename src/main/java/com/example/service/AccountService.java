package com.example.service;


import com.example.entity.Account;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void register(Account newAccount){
        if(newAccount.getUsername() == null || newAccount.getUsername().trim().isEmpty()){
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if(newAccount.getPassword() == null || newAccount.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if(newAccount.getPassword().length() < 4){
            throw new IllegalArgumentException("Password is too short");
        }
        if(accountRepository.existsByUsername(newAccount.getUsername())){
            throw new UsernameAlreadyExistsException("Username already taken");
        }
        accountRepository.save(newAccount);
    }

    public Account login(Account account) throws AuthenticationException {
        Optional<Account> fetchedAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(fetchedAccount.isPresent()){
            return fetchedAccount.get();
        }
        throw new AuthenticationException("Unauthorized");
    }
}
