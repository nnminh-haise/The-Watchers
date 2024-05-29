package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.watch_selling.model.Customer;
import com.example.watch_selling.dtos.CustomerProfileDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateProfileDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.service.CustomerService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path = "/api/customer")
public class CustomerController {
        @Autowired
        private CustomerService customerService;

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PostMapping("new")
        public ResponseEntity<ResponseDto<Customer>> createNewCustomerProfile(
                        @RequestHeader(value = "Authorization", required = false) String token,
                        @RequestBody CustomerProfileDto customerProfile) {
                Account customerAccount = getAssociatedAccount();

                System.out.println("dob: " + customerProfile.getDateOfBirth().toString());

                ResponseDto<Customer> res = customerService.createNewCustomer(
                                customerAccount.getId(), customerProfile);

                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("/profile")
        public ResponseEntity<ResponseDto<Customer>> readCustomerProfile(
                        @RequestHeader(value = "Authorization", required = false) String token) {
                Account customerAccount = getAssociatedAccount();

                System.out.println("id: " + customerAccount.getId());

                ResponseDto<Customer> res = customerService.findCustomerByAccountId(customerAccount.getId());

                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @PutMapping("/update")
        public ResponseEntity<ResponseDto<Customer>> updateProfile(
                        @RequestHeader(value = "Authorization", required = false) String token,
                        @RequestBody UpdateProfileDto updatedProfile) {
                Account account = getAssociatedAccount();

                ResponseDto<Customer> res = customerService.updateCustomerProfile(
                                account.getId(), updatedProfile);

                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @DeleteMapping("delete")
        public ResponseEntity<ResponseDto<String>> deleteCustomerProfile(
                        @RequestHeader(value = "Authorization", required = false) String token) {
                Account customerAccount = getAssociatedAccount();

                ResponseDto<String> res = customerService.updateDeleteStatusById(customerAccount.getId());

                return ResponseEntity.status(res.getStatus()).body(res);
        }

        private Account getAssociatedAccount() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return (Account) authentication.getPrincipal();
        }
}
