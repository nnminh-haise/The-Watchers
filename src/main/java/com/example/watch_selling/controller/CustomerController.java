package com.example.watch_selling.controller;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.dtos.CustomerInfoDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
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
    public ResponseEntity<ResponseDto<CustomerInfoDto>> authenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currentUserAccount = (Account)authentication.getPrincipal();

        Optional<Customer> currentCustomer = customerService.findByEmail(currentUserAccount.getEmail());
        if (!currentCustomer.isPresent()) {
            throw new UsernameNotFoundException("Customer's info is empty");
        }

        ResponseDto<CustomerInfoDto> response = new ResponseDto<>();
        response.setData(new CustomerInfoDto(currentCustomer.get()));
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto<String>> updateProfile(@RequestBody RequestDto<CustomerInfoDto> newCustomerInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account)authentication.getPrincipal();

        Optional<Customer> currentCustomer = customerService.findByEmail(customerAccount.getEmail());
        ResponseDto<String> response = new ResponseDto<>();
        if (!currentCustomer.isPresent()) {
            customerService.addNewCustomer(
                customerAccount.getEmail(), 
                newCustomerInfo.getData(), 
                customerAccount
            );

            response.setMessage("Customer's info is resigtered!");
            response.setStatusCode(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        
        try {
            customerService.updateCusomterInfo(
                                customerAccount.getEmail(),
                                newCustomerInfo.getData(),
                                customerAccount
                            );
        
        } catch (BadRequestException e) {
            
            response.setMessage(e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.setMessage("Customer's info is updated");
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
