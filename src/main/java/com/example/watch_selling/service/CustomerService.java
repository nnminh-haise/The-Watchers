package com.example.watch_selling.service;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
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

    public void updateCusomterInfo(String email, CustomerInfoDto newCustomerInfo, Account customerAccount) throws BadRequestException {
        Optional<Customer> currentCustomerInfo = customerRepository.findByEmail(email);

        if (!currentCustomerInfo.get().getEmail().equals(newCustomerInfo.getEmail())) {
            throw new BadRequestException("Cannot change customer's email!");
        }

        if (!currentCustomerInfo.get().getCmnd().equals(newCustomerInfo.getCmnd())) {
            throw new BadRequestException("Cannot change customer's CMND!");
        }

        if (!currentCustomerInfo.get().getAccount().getId().equals(customerAccount.getId())) {
            throw new BadRequestException("Cannot change customer's account!");
        }

        if (customerRepository.findEmailByPhoneNumber(newCustomerInfo.getSdt()).isPresent() && !customerRepository.findEmailByPhoneNumber(newCustomerInfo.getSdt()).get().equals(email)) {
            throw new BadRequestException("Phonenumber has been used!");
        }

        if (customerRepository.findEmailByMasothue(newCustomerInfo.getMasothue()).isPresent() && !customerRepository.findEmailByMasothue(newCustomerInfo.getMasothue()).get().equals(email)) {
            throw new BadRequestException("Masothue has been used!");
        }

        Customer newInfo = new Customer();
        newInfo.setCmnd(currentCustomerInfo.get().getCmnd());
        newInfo.setHo(newCustomerInfo.getHo());
        newInfo.setTen(newCustomerInfo.getTen());
        newInfo.setGioitinh(newCustomerInfo.getGioitinh());
        newInfo.setNgaysinh(newCustomerInfo.getNgaysinh());
        newInfo.setDiachi(newCustomerInfo.getDiachi());
        newInfo.setEmail(email);
        newInfo.setSdt(newCustomerInfo.getSdt());
        newInfo.setMasothue(newCustomerInfo.getMasothue());
        newInfo.setDaXoa(currentCustomerInfo.get().getDaXoa());
        newInfo.setAccount(customerAccount);
        newInfo.setHinhAnh(newCustomerInfo.getHinhAnh());

        customerRepository.update(customerAccount.getId(), newInfo);
    }
}