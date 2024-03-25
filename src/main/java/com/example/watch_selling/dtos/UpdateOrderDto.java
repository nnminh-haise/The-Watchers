package com.example.watch_selling.dtos;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class UpdateOrderDto {
    private String orderDate;

    private String name;

    private String address;

    private String phoneNumber;

    private String deliveryDate;

    public static ResponseDto<String> validDto(UpdateOrderDto dto) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<LocalDate> orderDate = CreateOrderDto.parseDate(dto.getOrderDate());
        if (!orderDate.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        Optional<LocalDate> deliveryDate = CreateOrderDto.parseDate(dto.getDeliveryDate());
        if (!deliveryDate.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        if (!CreateOrderDto.validName(dto.getName())) {
            return res.setMessage("Invalid name!");
        }

        if (!CreateOrderDto.validAddress(dto.getAddress())) {
            return res.setMessage("Invalid address!");
        }

        if (!CreateOrderDto.validPhonenumber(dto.getPhoneNumber())) {
            return res.setMessage("Invalid phonenumber!");
        }

        return res
                .setMessage("Valid!")
                .setStatus(HttpStatus.OK);
    }
}
