package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        Account customerAccount = getAssociatedAccount();

        ResponseDto<Customer> res = customerService.createNewCustomer(
            customerAccount.getId(), customerProfile.getData()
        );

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<Customer>> readCustomerProfile(
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Account customerAccount = getAssociatedAccount();

        ResponseDto<Customer> res = customerService.findCustomerByAccountId(customerAccount.getId());

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("/update")
    public ResponseEntity<ResponseDto<Customer>> updateProfile(
        @RequestHeader(value = "Authorization", required = false) String token,
        @RequestBody RequestDto<CustomerProfileDto> updatedProfile
    ) {
        Account account = getAssociatedAccount();

        ResponseDto<Customer> res = customerService.updateCustomerProfile(
            account.getId(), updatedProfile.getData()
        );

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteCustomerProfile(
        @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Account customerAccount = getAssociatedAccount();

        ResponseDto<String> res = customerService.updateDeleteStatusById(customerAccount.getId());

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    private Account getAssociatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }
}
