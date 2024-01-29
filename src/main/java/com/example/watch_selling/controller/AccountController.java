package com.example.watch_selling.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.AccountService;

@RestController
// @RequestMapping(path = "/api/v1")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // @PostMapping("sign-in")
    // public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest) {
    //     String email = signInRequest.getEmail();
    //     String password = signInRequest.getPassword();
    //     if (!isValidEmail(email)) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
    //     }

    //     Optional<Account> account = accountService.findByEmail(email);
    //     if (!account.isPresent()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No account assosiates with this email");
    //     }

    //     String storedPwd = account.get().getPassword();
    //     if (!storedPwd.equals(password)) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
    //     }

    //     return ResponseEntity.status(HttpStatus.OK).body("Sign in successfully");
    // }
}
