package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        HttpServletRequest request,
        @RequestBody RequestDto<CustomerProfileDto> customerProfile
    ) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        ResponseDto<Customer> response = customerService.createNewCustomer(
            customerAccount.getId(), customerProfile.getData()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @SuppressWarnings("null")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Customer>> readCustomerProfile(HttpServletRequest request) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        ResponseDto<Customer> response = customerService.findCustomerByEmail(customerAccount.getEmail());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @SuppressWarnings("null")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto<Customer>> updateProfile(
        HttpServletRequest request,
        @RequestBody RequestDto<CustomerProfileDto> newCustomerProfile
    ) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        ResponseDto<Customer> response = customerService.updateCustomerProfile(
            customerAccount.getId(), newCustomerProfile.getData()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteCustomerProfile(HttpServletRequest request) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        ResponseDto<String> response = customerService.updateDeleteStatusById(
            customerAccount.getId(), true
        );

        return ResponseEntity.status(response.getStatus()).body(response);
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
