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
import com.example.watch_selling.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartService cartService;

    /*
     * Find profile by associtated account ID
     * <p>
     * Each account will only have one associated profile.
     * Therefore we can consider account ID as a primary key
     * </p>
     */
    public ResponseDto<Customer> findCustomerByAccountId(UUID accountId) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            return res.setMessage("Invalid account ID!");
        }

        Optional<Customer> customer = customerRepository.findByAccountId(accountId);
        if (!customer.isPresent()) {
            return res
                    .setMessage("Cannot find any customer with the given account ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setData(customer.get())
                .setMessage("Successful!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<Customer> createNewCustomer(
            UUID accountId,
            CustomerProfileDto newProfile) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            return res.setMessage("Invalid account ID!");
        }

        Optional<Account> existingAccount = accountService.findById(accountId);
        if (!existingAccount.isPresent()) {
            return res.setMessage("Cannot find any account with the given account ID!");
        }

        // * Check for duplicated account ID
        if (customerRepository.findByAccountId(accountId).isPresent()) {
            return res.setMessage("The given account ID's profile is already exist!");
        }

        ResponseDto<String> dtoValidationResponse = CustomerProfileDto.validDto(newProfile);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setMessage(dtoValidationResponse.getMessage())
                    .setStatus(dtoValidationResponse.getStatus());
        }

        if (customerRepository
                .existProfileWithCitizenId(newProfile.getCitizenId(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated citizen ID! Invalid citizen ID!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        if (customerRepository
                .existProfileWithPhonenumber(newProfile.getPhoneNumber(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated phonenumber! Invalid phonenumber!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        if (customerRepository
                .existProfileWithTaxCode(newProfile.getTaxCode(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated tax code! Invalid tax code!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        Optional<Customer> newCustomer = CustomerProfileDto.toModel(newProfile);
        if (!newCustomer.isPresent()) {
            return res
                    .setMessage("Cannot convert DTO object into Model! [Customer profile]");
        }
        Customer cus = newCustomer.get();
        cus.setAccount(existingAccount.get());
        cus.setIsDeleted(false);
        cus.setId(null);

        customerRepository.save(cus);

        return res
                .setData(cus)
                .setMessage("Created new customer successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<Customer> updateCustomerProfile(
            UUID accountId,
            CustomerProfileDto updatedProfile) {
        ResponseDto<Customer> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<Customer> targetingCustomerProfile = customerRepository.findByAccountId(accountId);
        if (!targetingCustomerProfile.isPresent()) {
            return res
                    .setMessage("Cannot find any customer profile associated with the given account!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        if (updatedProfile.getJwt() != null) {
            return res.setMessage("JWT must be null!");
        }

        ResponseDto<String> dtoValidationResponse = CustomerProfileDto.validDto(updatedProfile);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setMessage(dtoValidationResponse.getMessage())
                    .setStatus(dtoValidationResponse.getStatus());
        }

        if (!updatedProfile.getCitizenId().equals(targetingCustomerProfile.get().getCitizenId())) {
            return res.setMessage("Cannot change citizen ID!");
        }

        if (!updatedProfile.getTaxCode().equals(targetingCustomerProfile.get().getTaxCode())) {
            return res.setMessage("Cannot change tax code!");
        }

        if (customerRepository
                .existProfileWithCitizenId(updatedProfile.getCitizenId(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated citizen ID! Invalid citizen ID!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        if (customerRepository
                .existProfileWithPhonenumber(updatedProfile.getPhoneNumber(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated phonenumber! Invalid phonenumber!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        if (customerRepository
                .existProfileWithTaxCode(updatedProfile.getTaxCode(), accountId).isPresent()) {
            return res
                    .setMessage("Duplicated tax code! Invalid tax code!")
                    .setStatus(HttpStatus.FORBIDDEN);
        }

        Optional<Customer> newProfileBuffer = CustomerProfileDto.toModel(updatedProfile);
        if (!newProfileBuffer.isPresent()) {
            return res.setMessage("Cannot create new profile from the given data!");
        }
        Customer newProfile = newProfileBuffer.get();
        newProfile.setId(targetingCustomerProfile.get().getId());
        newProfile.setAccount(targetingCustomerProfile.get().getAccount());

        customerRepository.updateCustomerProfileByAccountId(accountId, newProfile);

        return res
                .setData(newProfile)
                .setMessage("Customer profile updated successfully!")
                .setStatus(HttpStatus.OK);
    }

    /*
     * Deleting customer profile.
     * <p>
     * Deleteing customer profile will also deleting the associtated account of that
     * profile!
     * </p>
     */
    public ResponseDto<String> updateDeleteStatusById(UUID accountId) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            return res.setMessage("Invalid acount ID!");
        }

        Optional<Customer> targetingProfile = customerRepository.findByAccountId(accountId);
        if (!targetingProfile.isPresent()) {
            return res
                    .setMessage("Cannot find any customer with the given account ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        // try {
        // customerRepository.deleteProfileByAccountId(accountId);
        // accountService.deleteAccount(accountId);
        // cartService.deleteByAccountId(accountId);
        // }
        // catch (Exception e) {
        // return res
        // .setMessage(e.getMessage());
        // }

        customerRepository.deleteProfileByAccountId(accountId);
        accountService.deleteAccount(accountId);
        cartService.deleteByAccountId(accountId);

        return res
                .setMessage("Customer delete status updated successfully!")
                .setStatus(HttpStatus.OK);
    }
}