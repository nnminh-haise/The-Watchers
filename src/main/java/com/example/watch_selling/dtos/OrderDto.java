package com.example.watch_selling.dtos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.UUID;

import com.example.watch_selling.filters.CustomFilters;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private UUID id;

    private String orderDate;
    
    private String name;

    private String address;

    private String phoneNumber;

    private String deliveryDate;

    private String status;

    private UUID accountId;

    public Optional<Order> toModel(Boolean deleteStatus, Account account) {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        if (!CustomFilters.dateValidation(orderDate) || !CustomFilters.dateValidation(deliveryDate)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new Order(
                id, dateFormater.parse(orderDate), name, address, phoneNumber, dateFormater.parse(deliveryDate), status, deleteStatus, account
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
