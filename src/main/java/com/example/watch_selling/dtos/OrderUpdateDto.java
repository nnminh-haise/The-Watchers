package com.example.watch_selling.dtos;

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
}
