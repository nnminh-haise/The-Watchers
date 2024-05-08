package com.example.watch_selling.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.AccountInformation;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.AccountService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<AccountInformation>> findAccountInformation(
            @RequestHeader(value = "Authorization", required = false) String token) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        Optional<Account> res = accountService.findById(customerAccount.getId());
        if (res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null, "Cannot find any account with the given token!",
                HttpStatus.NOT_FOUND
            ));
        }
        AccountInformation data = new AccountInformation(
            res.get().getId(),
            res.get().getEmail(),
            token.substring(7)
        );

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            data, "Success!", HttpStatus.OK
        ));
    }
}
