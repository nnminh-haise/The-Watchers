package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CustomerProfileDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Customer;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseDto<Customer> findCustomerById(UUID id) {
        ResponseDto<Customer> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            response.setMessage("Cannot find any customer with the given ID!");
        }

        response.setData(customer.get());
        response.setMessage("Successful!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<Customer> findCustomerByEmail(String email) {
        ResponseDto<Customer> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (email == null || email.isEmpty() || email.isBlank()) {
            response.setMessage("Invalid email!");
            return response;
        }

        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (!customer.isPresent()) {
            response.setMessage("Cannot find any customer with the given email!");
            response.setStatus(HttpStatus.NOT_FOUND);
        }

        response.setData(customer.get());
        response.setMessage("Successful!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<Customer> createNewCustomer(UUID accountID, CustomerProfileDto customerProfile) {
        ResponseDto<Customer> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountID == null) {
            response.setMessage("Invalid account ID!");
            return response;
        }

        Optional<Account> associtatedAccount = accountRepository.findById(accountID);
        if (!associtatedAccount.isPresent()) {
            response.setMessage("Cannot find any account with the given account ID!");
            return response;
        }

        // * Check for duplicated account ID
        if (customerRepository.findByAccountId(accountID).isPresent()) {
            response.setMessage("The given account ID's profile is already exist!");
            return response;
        }

        ResponseDto<String> dtoValidationResponse = CustomerProfileDto.validDto(customerProfile);
        if (dtoValidationResponse.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            response.setMessage(dtoValidationResponse.getMessage());
            return response;
        }

        // * Check for duplicated citizen ID among the active profiles
        if (customerRepository.existProfileByCitizenId(customerProfile.getCitizenId()).isPresent()) {
            response.setMessage("This is citizen ID is used!");
            return response;
        }

        // * Check for duplicated citizen ID among the active profiles
        if (customerRepository.existProfileByPhonenumber(customerProfile.getPhoneNumber()).isPresent()) {
            response.setMessage("This is phonenumber is used!");
            return response;
        }

        Customer newCustomer = CustomerProfileDto.toModel(customerProfile);
        newCustomer.setAccount(associtatedAccount.get());
        newCustomer.setIsDeleted(false);
        newCustomer.setId(null);

        customerRepository.save(newCustomer);

        response.setData(newCustomer);
        response.setMessage("Created new customer successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<Customer> updateCustomerProfile(UUID id, CustomerProfileDto updateCustomerProfile) {
        ResponseDto<Customer> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<Customer> targetingCustomer = customerRepository.findById(id);
        if (!targetingCustomer.isPresent()) {
            response.setMessage("Cannot find any customer with the given ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        ResponseDto<String> dtoValidationResponse = CustomerProfileDto.validDto(updateCustomerProfile);
        if (dtoValidationResponse.getStatus().equals(HttpStatus.BAD_REQUEST)) {
            response.setMessage(dtoValidationResponse.getMessage());
            return response;
        }

        Customer updatedCustomer = customerRepository.updateCustomerProfileById(id, updateCustomerProfile);

        response.setData(updatedCustomer);
        response.setMessage("Customer profile updated successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<String> updateDeleteStatusById(UUID id, Boolean status) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<Customer> targetingCustomer = customerRepository.findById(id);
        if (!targetingCustomer.isPresent()) {
            response.setMessage("Cannot find any customer with the given ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        customerRepository.updateDeleteStatusById(id, status);

        response.setMessage("Customer delete status updated successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }
}