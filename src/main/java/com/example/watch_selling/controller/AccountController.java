package com.example.watch_selling.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.AccountInformation;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdatePasswordDto;
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
                System.out.println("email: " + res.get().getEmail());
                if (res.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                                        null, "Cannot find any account with the given token!",
                                        HttpStatus.NOT_FOUND));
                }
                AccountInformation data = new AccountInformation(
                                res.get().getId(),
                                res.get().getEmail(),
                                token.substring(7));

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
                                data, "Success!", HttpStatus.OK));
        }

        @PostMapping("/change-password")
        public ResponseEntity<ResponseDto<AccountInformation>> updatePassword(
                        @RequestHeader(value = "Authorization", required = false) String token,
                        @RequestBody UpdatePasswordDto payload) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Account user = (Account) authentication.getPrincipal();

                Optional<Account> account = this.accountService.findByEmail(user.getEmail());
                if (account.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                                        null, "Cannot find account!", HttpStatus.NOT_FOUND));
                }

                if (payload == null || !payload.getPassword().equals(payload.getConfirmPassword())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                                        null, "Invalid payload!", HttpStatus.BAD_REQUEST));
                }

                Boolean passwordVerifying = this.accountService.verifyPassword(user.getEmail(),
                                payload.getCurrentPassword());
                if (!passwordVerifying) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto<>(
                                        null, "Incorrect password!", HttpStatus.UNAUTHORIZED));
                }

                if (payload.getPassword().equals(payload.getCurrentPassword())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                                        null, "The new password should be different to the current password!",
                                        HttpStatus.BAD_REQUEST));
                }

                Boolean updatingPassword = this.accountService.updatePassword(user.getEmail(), payload.getPassword());
                if (!updatingPassword) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>(
                                        null, "Cannot update password!", HttpStatus.INTERNAL_SERVER_ERROR));
                }

                return ResponseEntity.ok().body(new ResponseDto<>(
                                new AccountInformation(
                                                user.getId(),
                                                user.getEmail(),
                                                token.substring(7)),
                                "Success", HttpStatus.OK));
        }
}
