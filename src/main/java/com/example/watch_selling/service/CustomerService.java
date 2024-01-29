package com.example.watch_selling.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.watch_selling.model.Customer;
import com.example.watch_selling.repository.CustomerRepository;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public List<Customer> allUsers() {
        List<Customer> customer = new ArrayList<>();

        customerRepository.findAll().forEach(customer::add);

        return customer;
    }
}