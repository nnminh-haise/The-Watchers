package com.example.watch_selling.controller;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.dtos.CustomerInfoDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.CustomerService;


@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerInfoDto> authenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currentUserAccount = (Account)authentication.getPrincipal();

        Optional<Customer> currentCustomer = customerService.findByEmail(currentUserAccount.getEmail());
        if (!currentCustomer.isPresent()) {
            throw new UsernameNotFoundException("Customer's info is empty");
        }

        return ResponseEntity.ok(new CustomerInfoDto(currentCustomer.get()));
    }
}
