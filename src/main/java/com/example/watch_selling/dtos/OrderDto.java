package com.example.watch_selling.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderDate;
    
    private String name;

    private String address;

    private String phoneNumber;

    private String deliveryDate;

    private String status;

    private UUID accountID;

    public static Boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }

    public static Boolean validAddress(String address) {
        return address != null && !address.isEmpty() && !address.isBlank();
    }

    public static Boolean validPhonenumber(String phonenumber) {
        return phonenumber != null && !phonenumber.isEmpty() && !phonenumber.isBlank();
    }

    public static Boolean validStatus(String status) {
        if (status == null) {
            return false;
        }

        List<String> values = new ArrayList<>();
        values.add("Chờ duyệt");
        values.add("Đang chuẩn bị hàng");
        values.add("Đang giao hàng");
        values.add("Hoàn thành");
        values.add("Đã huỷ");
        for (String value: values) {
            if (status.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static ResponseDto<Date> validOrderDate(String orderDate) {
        ResponseDto<Date> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (orderDate == null) {
            return res.setMessage("Invalid order date!");
        }

        Optional<Date> d = parseDate(orderDate);
        if (!d.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        return res
            .setMessage("Valid date!")
            .setData(d.get());
    }

    public static ResponseDto<Date> validDeliveryDate(String deliveryDate) {
        ResponseDto<Date> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (deliveryDate == null) {
            return res.setMessage("Invalid delivery date!");
        }

        Optional<Date> d = parseDate(deliveryDate);
        if (!d.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        return res
            .setMessage("Valid date!")
            .setData(d.get());
    }

    public static ResponseDto<String> validDto(OrderDto dto) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!OrderDto.validName(dto.getName())) {
            return res.setMessage("Invalid name!");
        }

        if (!OrderDto.validAddress(dto.getAddress())) {
            return res.setMessage("Invalid address!");
        }

        if (!OrderDto.validPhonenumber(dto.getPhoneNumber())) {
            return res.setMessage("Invalid phonenumber!");
        }

        if (!OrderDto.validStatus(dto.getStatus())) {
            return res.setMessage("Invalid status!");
        }

        Optional<Date> orderDate = OrderDto.parseDate(dto.getOrderDate());
        if (!orderDate.isPresent()) {
            return res.setMessage("Invalid order date format!");
        }

        Optional<Date> deliveryDate = OrderDto.parseDate(dto.getDeliveryDate());
        if (!deliveryDate.isPresent()) {
            return res.setMessage("Invalid delivery date format!");
        }

        if (orderDate.get().after(deliveryDate.get())) {
            return res.setMessage("Delivery date must be after the order date! Invalid delivery date!");
        }
        
        return res
            .setMessage("Valid DTO!")
            .setStatus(HttpStatus.OK);
    }

    public static Optional<Order> toModel(OrderDto dto, Boolean deleteStatus, Account account) {
        if (!OrderDto.validDto(dto).getStatus().equals(HttpStatus.OK)) {
            return Optional.empty();
        }

        Order o = new Order();
        o.setAccount(account);
        o.setOrderDate(OrderDto.parseDate(dto.getOrderDate()).get());
        o.setDeliveryDate(OrderDto.parseDate(dto.getDeliveryDate()).get());
        o.setName(dto.getName());
        o.setAddress(dto.getAddress());
        o.setPhoneNumber(dto.getPhoneNumber());
        o.setStatus(dto.getStatus());

        return Optional.of(o);
    }

    public static OrderDto toDto(Order o) {
        OrderDto dto = new OrderDto();

        dto.setName(o.getName());
        dto.setAddress(o.getAddress());
        dto.setOrderDate(o.getOrderDate().toString());
        dto.setDeliveryDate(o.getDeliveryDate().toString());
        dto.setPhoneNumber(o.getPhoneNumber());
        dto.setStatus(o.getStatus());
        dto.setAccountID(o.getAccount().getId());

        return dto;
    }

    public static Optional<Date> parseDate(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        }
        catch (ParseException e) {
            return Optional.empty();
        }
        return Optional.of(d);
    }
}
