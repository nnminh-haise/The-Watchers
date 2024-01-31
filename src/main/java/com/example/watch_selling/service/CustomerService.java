package com.example.watch_selling.service;

import java.util.Optional;

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

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomer(String customerEmail, CustomerInfoDto customerInfoDto, Account customerAccount) {
        Customer newCustomer = new Customer();

        newCustomer.setCmnd(customerInfoDto.getCmnd());
        newCustomer.setHo(customerInfoDto.getHo());
        newCustomer.setTen(customerInfoDto.getTen());
        newCustomer.setGioitinh(customerInfoDto.getGioitinh());
        newCustomer.setNgaysinh(customerInfoDto.getNgaysinh());
        newCustomer.setDiachi(customerInfoDto.getDiachi());
        newCustomer.setSdt(customerInfoDto.getSdt());
        newCustomer.setEmail(customerInfoDto.getEmail());
        newCustomer.setMasothue(customerInfoDto.getMasothue());
        newCustomer.setDaXoa(false);
        newCustomer.setAccount(customerAccount);
        newCustomer.setHinhAnh(customerInfoDto.getHinhAnh());

        customerRepository.save(newCustomer);
    }

    // public CustomerInfoDto updateCustomerInfo(String customerEmail, CustomerInfoDto currentCustomerInfo, Account customerAccount) {
    //     CustomerInfoDto newCustomerInfoDto = new CustomerInfoDto();

    //     return newCustomerInfoDto;
    // }
}