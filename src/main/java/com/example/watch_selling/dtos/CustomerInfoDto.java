package com.example.watch_selling.dtos;

import java.util.Date;

import com.example.watch_selling.model.Customer;

public class CustomerInfoDto {
    private String citizenId;

    private String firstName;

    private String lastName;

    private String gender;

    private Date dateOfBirth;

    private String address;

    private String phoneNumber;

    private String email;

    private String taxCode;

    private String photo;

    private String jwt;

    public CustomerInfoDto() {
    }

    public CustomerInfoDto(String citizenId, String firstName, String lastName, String gender, Date dateOfBirth, String address, String phoneNumber, String email, String taxCode) {
        this.citizenId = citizenId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.taxCode = taxCode;
    }

    public CustomerInfoDto(Customer customer) {
        this.citizenId = customer.getCitizenId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.gender = customer.getGender();
        this.dateOfBirth = customer.getDateOfBirth();
        this.address = customer.getAddress();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.taxCode = customer.getTaxCode();
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Customer makeCustomer() {
        Customer newCustomer = new Customer();

        newCustomer.setCitizenId(citizenId);
        newCustomer.setEmail(email);
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setGender(gender);
        newCustomer.setDateOfBirth(dateOfBirth);
        newCustomer.setAddress(address);
        newCustomer.setPhoneNumber(phoneNumber);
        newCustomer.setTaxCode(taxCode);
        newCustomer.setPhoto(photo);

        return newCustomer;
    }
}
