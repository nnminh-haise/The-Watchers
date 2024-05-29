package com.example.watch_selling.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotNull(message = "Receiver's name must not be empty")
    private String name;

    @NotNull(message = "Receiver's address must not be empty")
    private String address;

    @NotNull(message = "Receiver's phone number must not be empty")
    @Size(min = 10, max = 10, message = "Receiver's phone number must be exact 10 digit")
    private String phoneNumber;

    @NotNull(message = "Order details must not be empty")
    private List<UUID> orderDetails;

    @NotNull(message = "Tax code must not be empty")
    private String taxCode;
}
