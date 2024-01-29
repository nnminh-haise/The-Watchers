package com.example.watch_selling.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.service.CustomerService;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
    private CustomerService CustomerService;

    @Autowired
    public CustomerController(CustomerService CustomerService) {
        this.CustomerService = CustomerService;
    }

    /**
     * TODO: Fix this into get all customers
     * Get all customers
     * @param customer
     * @return all customers in json format
     */
    @GetMapping("")
    public ResponseEntity<String> getAllCustomersAccount() {
        return ResponseEntity.status(HttpStatus.FOUND).body("Found customer");
    }

    @GetMapping("/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> optionalCustomer = CustomerService.findByEmail(email);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalCustomer.get());
    }
}
