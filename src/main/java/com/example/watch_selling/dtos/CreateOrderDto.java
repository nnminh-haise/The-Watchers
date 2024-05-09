package com.example.watch_selling.dtos;

import java.time.LocalDate;
import java.util.Optional;

import com.example.watch_selling.helpers.DateParser;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDto {
    @NotNull(message = "Order date must not be empty")
    private String orderDate;

    @NotNull(message = "Receiver's name must not be empty")
    private String name;

    @NotNull(message = "Receiver's address must not be empty")
    private String address;

    @NotNull(message = "Receiver's phone number must not be empty")
    @Size(min = 10, max = 10, message = "Receiver's phone number must be exact 10 digit")
    private String phoneNumber;

    private String deliveryDate;

    public static Boolean isValidated(CreateOrderDto dto) {
        if (dto == null) {
            return false;
        }

        Optional<LocalDate> orderDate = DateParser.parse(dto.getOrderDate());
        if (orderDate.isEmpty()) {
            System.out.println("[Create order dto] DTO validator: Cannot parse order date!");
            return false;
        }

        // TODO: Clean this logic
        LocalDate deliveryDate;
        if (dto.getDeliveryDate() == null) {
            deliveryDate = orderDate.get().plusDays(3);
        } else {
            Optional<LocalDate> deliveryDateBuffer = DateParser.parse(dto.getOrderDate());
            if (deliveryDateBuffer.isEmpty()) {
                System.out.println("[Create order dto] DTO validator: Cannot parse delivery date!");
                return false;
            }
            deliveryDate = deliveryDateBuffer.get();
        }

        if (deliveryDate.isBefore(orderDate.get())) {
            return false;
        }

        return true;
    }

    // TODO: separate the dto validation logic and update dto mapping to model
    public static Optional<Order> produce(CreateOrderDto dto, Account ownerAccount) {
        if (dto == null) {
            return Optional.empty();
        }

        Optional<LocalDate> orderDate = DateParser.parse(dto.getOrderDate());
        if (orderDate.isEmpty()) {
            System.out.println("[Create order dto] Entity producer: Cannot parse order date!");
            return Optional.empty();
        }

        // TODO: Clean this logic
        LocalDate deliveryDate;
        if (dto.getDeliveryDate() == null) {
            deliveryDate = orderDate.get().plusDays(3);
        } else {
            Optional<LocalDate> deliveryDateBuffer = DateParser.parse(dto.getOrderDate());
            if (deliveryDateBuffer.isEmpty()) {
                System.out.println("[Create order dto] Entity producer: Cannot parse delivery date!");
                return Optional.empty();
            }
            deliveryDate = deliveryDateBuffer.get();
        }

        if (deliveryDate.isBefore(orderDate.get())) {
            return Optional.empty();
        }

        Order order = new Order();
        order.setName(dto.getName());
        order.setAddress(dto.getAddress());
        order.setPhoneNumber(dto.getPhoneNumber());
        order.setOrderDate(orderDate.get());
        order.setDeliveryDate(deliveryDate);
        order.setStatus(OrderStatus.PENDING.toString());
        order.setIsDeleted(false);
        order.setAccount(ownerAccount);
        return Optional.of(order);
    }
}
