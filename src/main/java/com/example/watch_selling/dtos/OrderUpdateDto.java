package com.example.watch_selling.dtos;

import java.util.Date;
import java.util.Optional;

import org.hibernate.validator.cfg.defs.pl.REGONDef;
import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {
    private String orderDate;
    
    private String name;

    private String address;

    private String phoneNumber;

    private String deliveryDate;

    public static ResponseDto<String> validDto(OrderUpdateDto dto) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<Date> orderDate = OrderDto.parseDate(dto.getOrderDate());
        if (!orderDate.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        Optional<Date> deliveryDate = OrderDto.parseDate(dto.getDeliveryDate());
        if (!deliveryDate.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        if (!OrderDto.validName(dto.getName())) {
            return res.setMessage("Invalid name!");
        }

        if (!OrderDto.validAddress(dto.getAddress())) {
            return res.setMessage("Invalid address!");
        }

        if (!OrderDto.validPhonenumber(dto.getPhoneNumber())) {
            return res.setMessage("Invalid phonenumber!");
        }

        return res
            .setMessage("Valid!")
            .setStatus(HttpStatus.OK);
    }
}
