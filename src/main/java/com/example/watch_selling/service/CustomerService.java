package com.example.watch_selling.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CustomerProfileDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateProfileDto;
import com.example.watch_selling.helpers.DateParser;
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
            UpdateProfileDto dto) {
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

        if (customerRepository.existProfileWithPhonenumber(
                dto.getPhoneNumber(), accountId).isPresent()) {
            return res
                    .setMessage("Số điện thoại đã được sử dụng! Vui lòng sử dụng số điện thoại khác")
                    .setStatus(HttpStatus.BAD_REQUEST);
        }

        if (customerRepository.existProfileWithCitizenId(dto.getCitizenId(), accountId).isPresent()) {
            return res
                    .setMessage("CCCD đã được đăng kí! Vui lòng sử dụng CCCD khác!")
                    .setStatus(HttpStatus.BAD_REQUEST);
        }

        Optional<LocalDate> dob = DateParser.parse(dto.getDateOfBirth());
        if (!dob.isPresent()) {
            return res.setMessage("Lồi định dạng ngày sinh!");
        }
        if (dob.get().isAfter(LocalDate.now())) {
            return res.setMessage("Lỗi ngày sinh vô nghĩa! Ngày sinh không thể lớn hơn ngày hiện tại!");
        }

        Customer newProfile = targetingCustomerProfile.get();
        if (dto.getFirstName() != null)
            newProfile.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            newProfile.setLastName(dto.getLastName());
        if (dto.getAddress() != null)
            newProfile.setAddress(dto.getAddress());
        if (dto.getGender() != null)
            newProfile.setGender(dto.getGender());
        if (dto.getPhoneNumber() != null)
            newProfile.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPhoto() != null)
            newProfile.setPhoto(dto.getPhoto());
        if (dto.getCitizenId() != null)
            newProfile.setCitizenId(dto.getCitizenId());
        newProfile.setDateOfBirth(dob.get());

        customerRepository.updateCustomerProfileByAccountId(accountId, newProfile);

        return res
                .setData(newProfile)
                .setMessage("Customer profile updated successfully!")
                .setStatus(HttpStatus.OK);
    }

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

        customerRepository.deleteProfileByAccountId(accountId);
        accountService.deleteAccount(accountId);
        cartService.deleteByAccountId(accountId);

        return res
                .setMessage("Customer delete status updated successfully!")
                .setStatus(HttpStatus.OK);
    }
}