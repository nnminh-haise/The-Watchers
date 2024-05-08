package com.example.watch_selling.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.LoginDto;
import com.example.watch_selling.dtos.RegisterDto;
import com.example.watch_selling.dtos.RegisterResponse;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.AuthenticationService;
import com.example.watch_selling.service.JwtService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

class LoginResponse {
    private String email;

    private String token;

    private long expiresIn;

    public String getEmail() {
        return email;
    }

    public LoginResponse setEmail(String email) {
        this.email = email;
        return this;
    }

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

// TODO: Set exprire jwt time
@RestController
@RequestMapping(path = "/api/auth")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sign up success!"),
            @ApiResponse(responseCode = "401", description = "Unauthorize!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<RegisterResponse>> register(@RequestBody RegisterDto registerAccountDto) {
        Account registeredAccount = authenticationService.signup(registerAccountDto);

        String token = jwtService.generateToken(registeredAccount);

        ResponseDto<RegisterResponse> response = new ResponseDto<>(
                new RegisterResponse(
                        registeredAccount.getId(),
                        registeredAccount.getEmail(),
                        token),
                "Success!",
                HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sign in success!"),
            @ApiResponse(responseCode = "401", description = "Unauthorize!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<LoginResponse>> authenticate(@RequestBody LoginDto loginDto) {
        Account authenticatedUser = authenticationService.authenticate(loginDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setEmail(loginDto.getEmail())
                .setExpiresIn(jwtService.getExpirationTime());

        ResponseDto<LoginResponse> response = new ResponseDto<>();
        response.setData(loginResponse);
        response.setStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}