package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.service.CustomerService;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping(path="/api/customer")
public class UserController {
    private CustomerService userService;

    @Autowired
    public UserController(CustomerService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup") // http://localhost:8080/api/customer/signup
    public ResponseEntity<String> signUp(@RequestBody Customer customer) {
        if (userService.findByEmail(customer.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken");
        }

        Customer createdCustomer = userService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }
}