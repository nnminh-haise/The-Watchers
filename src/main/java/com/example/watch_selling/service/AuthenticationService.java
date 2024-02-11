package com.example.watch_selling.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.LoginDto;
import com.example.watch_selling.dtos.RegisterDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.repository.AccountRepository;

@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;

    private final AccountService accountService;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        AccountRepository accountRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        AccountService accountService
    ) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    public Account signup(RegisterDto input) {
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            throw new BadCredentialsException("Email is required");
        }

        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new BadCredentialsException("Password is required");
        }

        if (input.getConfirmPassword() == null || input.getConfirmPassword().isEmpty()) {
            throw new BadCredentialsException("Confirm password is required");
        }

        if (!isValidEmail(input.getEmail())) {
            throw new BadCredentialsException("Email is invalid");
        }

        if (!input.getPassword().equals(input.getConfirmPassword())) {
            throw new BadCredentialsException("Password and confirm password do not match");
        }

        if (accountService.findByEmail(input.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is already taken");
        }

        Account account = new Account();
        account.setEmail(input.getEmail());
        account.setPassword(passwordEncoder.encode(input.getPassword()));
        account.setIsDeleted(false);

        return accountRepository.save(account);
    }

    public Account authenticate(LoginDto input) {
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            throw new BadCredentialsException("Email is required");
        }

        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new BadCredentialsException("Password is required");
        }

        if (!isValidEmail(input.getEmail())) {
            throw new BadCredentialsException("Email is invalid");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return accountRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    private Boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }
}
