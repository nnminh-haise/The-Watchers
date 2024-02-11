package com.example.watch_selling.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CustomerInfoDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Customer;
import com.example.watch_selling.repository.CustomerRepository;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Integer createCustomerProfile(Account customerAccount, CustomerInfoDto customerProfile) throws BadRequestException {
        if (!isValidEmail(customerAccount.getEmail())) {
            throw new BadRequestException("Invalid email!");
        }

        Optional<Customer> customer = customerRepository.findByEmail(customerAccount.getEmail());
        if (customer.isPresent()) {
            return HttpStatus.CONFLICT.value();
        }

        if (!customerProfile.getEmail().equals(customerAccount.getEmail())) {
            throw new BadRequestException("Wrong customer' email!");
        }

        Customer newCustomer = customerProfile.makeCustomer();
        newCustomer.setAccount(customerAccount);
        newCustomer.setIsDeleted(false);
        customerRepository.save(newCustomer);

        return HttpStatus.CREATED.value();
    }

    public Integer updateCustomerProfile(Account customerAccount, CustomerInfoDto newCustomerProfile) throws BadRequestException {
        if (!isValidEmail(customerAccount.getEmail())) {
            throw new BadRequestException("Invalid email!");
        }

        Optional<Customer> customer = customerRepository.findByEmail(customerAccount.getEmail());
        if (!customer.isPresent()) {
            return HttpStatus.NOT_FOUND.value();
        }
        else if (customer.get().getIsDeleted() == true) {
            return HttpStatus.NOT_FOUND.value();
        }

        if (!customer.get().getEmail().equals(newCustomerProfile.getEmail())) {
            throw new BadRequestException("Unmatch email! Cannot change email!"); 
        }

        if (!customer.get().getCitizenId().equals(newCustomerProfile.getCitizenId())) {
            throw new BadRequestException("Unmatch citizen id! Cannot change citizen id!"); 
        }

        if (!customer.get().getTaxCode().equals(newCustomerProfile.getTaxCode())) {
            throw new BadRequestException("Unmatch tax code! Cannot change tax code!"); 
        }

        // * Customer(id, account_id, is_deleted) is kept original. Therefore there is no need to rewrite it!
        customerRepository.update(customer.get().getId(), newCustomerProfile.makeCustomer());

        return HttpStatus.OK.value();
    }

    @SuppressWarnings("null")
    public Integer deleteCustomerInfo(String email) throws BadRequestException {
        if (!isValidEmail(email)) {
            throw new BadRequestException("Invalid email!");
        }

        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (!customer.isPresent()) {
            return HttpStatus.NOT_FOUND.value();
        }
        else if (customer.get().getIsDeleted() == true) {
            return HttpStatus.NOT_FOUND.value();
        }

        //customerRepository.delete(customer.get());
        customerRepository.updateIsDeleted(customer.get().getEmail(), true);

        return HttpStatus.OK.value();
    }

    // * Private methods ---

    private Boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // private Boolean isEmailTaken(String email) {
    //     Optional<Customer> customer = customerRepository.findByEmail(email);
    //     return customer.isPresent();
    // }

    // private Boolean isCitizenIdTaken(String citizenId) {
    //     Optional<Customer> customer = customerRepository.findByCitizenId(citizenId);
    //     return customer.isPresent();
    // }

    // private Boolean isTaxCodeTaken(String taxCode) {
    //     Optional<Customer> customer = customerRepository.findByTaxCode(taxCode);
    //     return customer.isPresent();
    // }
}