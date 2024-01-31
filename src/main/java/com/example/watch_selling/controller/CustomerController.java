package com.example.watch_selling.controller;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/test")
    public String testPost() {
        return "test complete";
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

    @PatchMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody CustomerInfoDto customerInfoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account)authentication.getPrincipal();

        Optional<Customer> currentCustomer = customerService.findByEmail(customerAccount.getEmail());
        if (!currentCustomer.isPresent()) {
            customerService.addNewCustomer(customerAccount.getEmail(), customerInfoDto, customerAccount);
        }
        else {
            // customerService.updateCustomerInfo(customerAccount.getEmail(), customerInfoDto, customerAccount.getId());
        }
        
        return ResponseEntity.ok("Customer's info is updated");
    }
}
