package com.example.watch_selling.controller;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
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
import com.example.watch_selling.dtos.CustomerInfoDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<CustomerInfoDto>> createNewCustomerProfile(
        HttpServletRequest request,
        @RequestBody RequestDto<CustomerInfoDto> customerProfile
    ) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        // ! Temporary solution
        Optional<Customer> cus = customerService.getCustomerByEmail(customerAccount.getEmail());
        if (cus.isPresent() && cus.get().getIsDeleted() == true) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto<>(
                null,
                "Cannot create new profile!",
                "Forbidden email!",
                HttpStatus.CONFLICT.value())
            );
        }

        try {
            Integer customerProfileCreated = customerService.createCustomerProfile(customerAccount, customerProfile.getData());
            if (customerProfileCreated == HttpStatus.CONFLICT.value()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto<>(
                    customerProfile.getData(),
                    "Customer's profile existed! Cannot create new profile!",
                    HttpStatus.CONFLICT.value())
                );
            }
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                null,
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value())
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(
            customerProfile.getData(),
            "Created customer's profile successfully!",
            HttpStatus.CREATED.value())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDto<CustomerInfoDto>> readCustomerProfile(HttpServletRequest request) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        System.out.println("LOG - " + this.getAuthorizationToken(request));

        Optional<Customer> customer = customerService.getCustomerByEmail(customerAccount.getEmail());
        if (!customer.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find customer's profile associated with this account!",
                HttpStatus.NOT_FOUND.value())
            );
        }
        else if (customer.get().getIsDeleted() == true) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                null,
                "Cannot find customer's profile associated with this account!",
                HttpStatus.NOT_FOUND.value())
            );
        }

        CustomerInfoDto responseBody = new CustomerInfoDto(customer.get());
        responseBody.setJwt(this.getAuthorizationToken(request));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            responseBody,
            "Successfully read customer's profile!",
            HttpStatus.OK.value())
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseDto<CustomerInfoDto>> updateProfile(
        HttpServletRequest request,
        @RequestBody RequestDto<CustomerInfoDto> newCustomerInfo
    ) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        System.out.println("Passed through token validation!");

        try {
            Integer customerProfileUpdated = customerService.updateCustomerProfile(customerAccount, newCustomerInfo.getData());
            if (customerProfileUpdated == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                    new CustomerInfoDto(customerService.getCustomerByEmail(customerAccount.getEmail()).get()),
                    "Cannot find customer profile with the associated account!",
                    HttpStatus.NOT_FOUND.value())
                );
            }
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                new CustomerInfoDto(customerService.getCustomerByEmail(customerAccount.getEmail()).get()),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value())
            );
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseDto<>(
            new CustomerInfoDto(customerService.getCustomerByEmail(customerAccount.getEmail()).get()),
            "Customer's profile update successfully!",
            HttpStatus.OK.value())
        );
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<CustomerInfoDto>> deleteCustomerProfile(HttpServletRequest request) {
        validateToken(request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account customerAccount = (Account) authentication.getPrincipal();

        try {
            Integer deleteProcessComplete = customerService.deleteCustomerInfo(customerAccount.getEmail());
            if (deleteProcessComplete == HttpStatus.NOT_FOUND.value()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(
                    null,
                    "Cannot remove customer's profile! Customer's profile is not found",
                    HttpStatus.NOT_FOUND.value())
                );
            }
        }
        catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                new CustomerInfoDto(customerService.getCustomerByEmail(customerAccount.getEmail()).get()),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value())
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(
            null,
            "Successfully removed customer's profile",
            HttpStatus.OK.value())
        );
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
