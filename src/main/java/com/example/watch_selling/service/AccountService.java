package com.example.watch_selling.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.model.Account;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<List<Account>> findAll() {
        return Optional.ofNullable(accountRepository.findAll());
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
