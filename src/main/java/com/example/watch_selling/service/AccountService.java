package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDto<List<Account>> findAllAcounts() {
        ResponseDto<List<Account>> res = new ResponseDto<>(null, "", HttpStatus.NOT_FOUND);
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            return res
                    .setMessage("Cannot find any accounts!");
        }

        return res
                .setMessage("Success!")
                .setStatus(HttpStatus.OK)
                .setData(accounts);
    }

    public Optional<Account> findByEmail(String email) {
        if (email.isBlank() || email.isEmpty() || email == null) {
            return Optional.empty();
        }
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }
        return accountRepository.findById(id);
    }

    public Boolean updatePassword(String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        Integer affectedRecords = this.accountRepository.updatePassword(email,
                hashedPassword);
        return affectedRecords > 1 ? false : true;
    }

    public ResponseDto<String> deleteAccount(UUID id) {
        ResponseDto<String> res = new ResponseDto<>();

        accountRepository.deleteById(id);
        return res
                .setMessage("Success!")
                .setStatus(HttpStatus.OK);
    }
}
