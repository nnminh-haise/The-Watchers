package com.example.watch_selling.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.LoginDto;
import com.example.watch_selling.dtos.RegisterDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.AuthenticationService;
import com.example.watch_selling.service.JwtService;

class LoginResponse {
    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }

	public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Account> register(@RequestBody RequestDto<RegisterDto> registerAccountDto) {
        Account registeredAccount = authenticationService.signup(registerAccountDto.getData());

        return ResponseEntity.ok(registeredAccount);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody RequestDto<LoginDto> loginDto) {
        Account authenticatedUser = authenticationService.authenticate(loginDto.getData());
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}