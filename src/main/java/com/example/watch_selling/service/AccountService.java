package com.example.watch_selling.service;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<List<Account>> findAll() {
        return Optional.ofNullable(accountRepository.findAll());
    }

    public ResponseDto<List<Account>> findAllAcounts() {
        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            return new ResponseDto<>(
                null,
                "Cannot find any accounts!",
                HttpStatus.NOT_FOUND.value()
            );
        }

        return new ResponseDto<>(
            accounts,
            "",
            HttpStatus.OK.value()
        );
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public ResponseDto<String> updateDeleteStatus(String email, Boolean status) {
        if (
            email.equals(null) ||
            email.isBlank() ||
            !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        ) {
            return new ResponseDto<>(
                null,
                "Invalid email!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        accountRepository.updateDeleteStatus(email, status);
        return new ResponseDto<>(
            null,
            "",
            HttpStatus.OK.value()
        );
    }
}
