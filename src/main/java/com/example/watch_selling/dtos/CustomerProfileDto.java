package com.example.watch_selling.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileDto {
    private String citizenId;

    private String firstName;

    private String lastName;

    private String gender;

    private String dateOfBirth;

    private String address;

    private String phoneNumber;

    private String taxCode;

    private String photo;

    private String jwt = null;

    public static Boolean validCitizenId(String citizenId) {
        if (citizenId == null ||
            citizenId.isEmpty() ||
            citizenId.isBlank() ||
            citizenId.length() != 10
        ) {
            return false;
        }

        return true;
    }

    public static Boolean validTaxCode(String taxCode) {
        if (taxCode == null ||
            taxCode.isEmpty() ||
            taxCode.isBlank() ||
            taxCode.length() < 10 ||
            taxCode.length() > 13
        ) {
            return false;
        }

        return true;
    }

    public static Boolean validFirstName(String firstName) {
        if (firstName == null ||
            firstName.isEmpty() ||
            firstName.isBlank()
        ) {
            return false;
        }

        return true;
    }

    public static Boolean validLastName(String lastName) {
        if (lastName == null ||
            lastName.isEmpty() ||
            lastName.isBlank()
        ) {
            return false;
        }

        return true;
    }

    // TODO: Consider changing to Enum class
    public static Boolean validGender(String gender) {
        if (gender.equals("Nam") == false &&
            gender.equals("Nữ") == false
        ) {
            return false;
        }

        return true;
    }

    public static Boolean validDateOfBirth(String dateOfBirth) {
        try {
            new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        }
        catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static Boolean validAddress(String address) {
        if (address == null ||
            address.isEmpty() ||
            address.isBlank()
        ) {
            return false;
        }

        return true;
    }

    public static Boolean validPhonenumber(String phonenumber) {
        if (phonenumber == null ||
            phonenumber.isEmpty() ||
            phonenumber.isBlank() ||
            phonenumber.length() != 10
        ) {
            return false;
        }

        return true;
    }

    public static ResponseDto<String> validDto(CustomerProfileDto dto) {
        ResponseDto<String> response = new ResponseDto<>(
            null, "", HttpStatus.BAD_REQUEST
        );

        if (dto == null) {
            return response.setMessage("Invalid DTO");
        }

        if (!CustomerProfileDto.validCitizenId(dto.getCitizenId())) {
            response.setMessage("Invalid citized ID!");
            return response;
        }

        if (!CustomerProfileDto.validFirstName(dto.getFirstName())) {
            response.setMessage("Invalid first name!");
            return response;
        }

        if (!CustomerProfileDto.validLastName(dto.getLastName())) {
            response.setMessage("Invalid last name!");
            return response;
        }

        if (!CustomerProfileDto.validGender(dto.getGender())) {
            response.setMessage("Invalid gender!");
            return response;
        }

        if (!CustomerProfileDto.validPhonenumber(dto.getPhoneNumber())) {
            response.setMessage("Invalid phonenumber!");
            return response;
        }

        if (!CustomerProfileDto.validTaxCode(dto.getTaxCode())) {
            response.setMessage("Invalid tax code!");
            return response;
        }

        if (!CustomerProfileDto.validAddress(dto.getAddress())) {
            response.setMessage("Invalid address!");
            return response;
        }

        if (!CustomerProfileDto.validDateOfBirth(dto.getDateOfBirth())) {
            response.setMessage("Invalid phonenumber!");
            return response;
        }

        response.setStatus(HttpStatus.OK);
        return response;
    }

    /*
     * Creating new Customer object from the CustomerProfileDto object.
     * <p>
     * This method should only use for createing new Customer from the DTO object.
     * </p>
     */
    public static Optional<Customer> toModel(CustomerProfileDto dto) {
        Customer newCustomer = new Customer();

        Date dob = null;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDateOfBirth());
        }
        catch (ParseException e) {
            return Optional.empty();
        }

        newCustomer.setCitizenId(dto.getCitizenId());
        newCustomer.setFirstName(dto.getFirstName());
        newCustomer.setLastName(dto.getLastName());
        newCustomer.setGender(dto.getGender());
        newCustomer.setDateOfBirth(dob);
        newCustomer.setAddress(dto.getAddress());
        newCustomer.setPhoneNumber(dto.getPhoneNumber());
        newCustomer.setTaxCode(dto.getTaxCode());
        newCustomer.setPhoto(dto.getPhoto());

        return Optional.of(newCustomer);
    }
}
