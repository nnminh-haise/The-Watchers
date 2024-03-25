package com.example.watch_selling.dtos;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateOrderDto {
    private String orderDate;

    private String name;

    private String address;

    private String phoneNumber;

    private String deliveryDate;

    public static Boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }

    public static Boolean validAddress(String address) {
        return address != null && !address.isEmpty() && !address.isBlank();
    }

    public static Boolean validPhonenumber(String phonenumber) {
        return phonenumber != null && !phonenumber.isEmpty() && !phonenumber.isBlank();
    }

    public static ResponseDto<LocalDate> validOrderDate(String orderDate) {
        ResponseDto<LocalDate> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (orderDate == null) {
            return res.setMessage("Invalid order date!");
        }

        Optional<LocalDate> d = parseDate(orderDate);
        if (!d.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        return res
                .setMessage("Valid date!")
                .setData(d.get());
    }

    public static ResponseDto<LocalDate> validDeliveryDate(String deliveryDate) {
        ResponseDto<LocalDate> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (deliveryDate == null) {
            return res.setMessage("Invalid delivery date!");
        }

        Optional<LocalDate> d = parseDate(deliveryDate);
        if (!d.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        return res
                .setMessage("Valid date!")
                .setData(d.get());
    }

    public static ResponseDto<String> validDto(CreateOrderDto dto) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!CreateOrderDto.validName(dto.getName())) {
            return res.setMessage("Invalid name!");
        }

        if (!CreateOrderDto.validAddress(dto.getAddress())) {
            return res.setMessage("Invalid address!");
        }

        if (!CreateOrderDto.validPhonenumber(dto.getPhoneNumber())) {
            return res.setMessage("Invalid phonenumber!");
        }

        Optional<LocalDate> orderDate = CreateOrderDto.parseDate(dto.getOrderDate());
        if (!orderDate.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        Optional<LocalDate> deliveryDate = CreateOrderDto.parseDate(dto.getDeliveryDate());
        if (!deliveryDate.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        if (orderDate.get().isAfter(deliveryDate.get())) {
            return res.setMessage("Delivery date must be after the order date! Invalid delivery date!");
        }

        return res
                .setMessage("Valid DTO!")
                .setStatus(HttpStatus.OK);
    }

    public static Optional<Order> toModel(CreateOrderDto dto, String status, Boolean deleteStatus, Account account) {
        if (!CreateOrderDto.validDto(dto).getStatus().equals(HttpStatus.OK)) {
            return Optional.empty();
        }

        Order o = new Order();
        o.setAccount(account);
        o.setOrderDate(CreateOrderDto.parseDate(dto.getOrderDate()).get());
        o.setDeliveryDate(CreateOrderDto.parseDate(dto.getDeliveryDate()).get());
        o.setName(dto.getName());
        o.setAddress(dto.getAddress());
        o.setPhoneNumber(dto.getPhoneNumber());
        o.setStatus(status);
        o.setIsDeleted(deleteStatus);

        return Optional.of(o);
    }

    public static CreateOrderDto toDto(Order o) {
        CreateOrderDto dto = new CreateOrderDto();

        dto.setName(o.getName());
        dto.setAddress(o.getAddress());
        dto.setOrderDate(o.getOrderDate().toString());
        dto.setDeliveryDate(o.getDeliveryDate().toString());
        dto.setPhoneNumber(o.getPhoneNumber());

        return dto;
    }

    public static Optional<LocalDate> parseDate(String date) {
        LocalDate d = null;
        try {
            d = LocalDate.parse(date);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(d);
    }
}
