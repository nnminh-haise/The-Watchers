package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.dtos.CustomerProfileDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<Customer>> createNewCustomerProfile(
        @RequestHeader(value = "Authorization", required = false) String token,
        @RequestBody RequestDto<CustomerProfileDto> customerProfile
    ) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.FORBIDDEN);
        if (token == null) {
            return ResponseEntity
                .status(res.getStatus())
                .body(res
                    .setMessage("Invalid token"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        res = customerService.createNewCustomer(
            customerAccount.getId(), customerProfile.getData()
        );

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Customer>> readCustomerProfile(
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.FORBIDDEN);
        if (token == null) {
            return ResponseEntity.status(res.getStatus()).body(res
                .setMessage("Invalid token")
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        res = customerService.findCustomerByAccountId(customerAccount.getId());

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto<Customer>> updateProfile(
        @RequestHeader(value = "Authorization", required = false) String token,
        @RequestBody RequestDto<CustomerProfileDto> updatedProfile
    ) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.FORBIDDEN);
        if (token == null) {
            return ResponseEntity.status(res.getStatus()).body(res
                .setMessage("Invalid token")
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        res = customerService.updateCustomerProfile(
            account.getId(), updatedProfile.getData()
        );

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteCustomerProfile(
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.FORBIDDEN);
        if (token == null) {
            return ResponseEntity.status(res.getStatus()).body(res
                .setMessage("Invalid token")
            );
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        res = customerService.updateDeleteStatusById(customerAccount.getId());

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    // * Private methods ----

    private void validateToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.startsWith("Bearer ") == false) {
            throw new BadCredentialsException("Invalid token!");
        }
    }

    private String getAuthorizationToken(HttpServletRequest request) {
        this.validateToken(request);
        return request.getHeader("Authorization").substring(7);
    }
}
