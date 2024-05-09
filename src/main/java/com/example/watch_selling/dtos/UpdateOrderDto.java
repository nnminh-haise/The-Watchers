package com.example.watch_selling.dtos;

import java.time.LocalDate;
import java.util.Optional;

import com.example.watch_selling.helpers.DateParser;
import com.example.watch_selling.model.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {
    private String orderDate;

    private String name;

    private String address;

    @Size(min = 10, max = 10, message = "Receiver's phone number must be exact 10 digit")
    private String phoneNumber;

    private String deliveryDate;

    public static Optional<Order> produce(UpdateOrderDto dto, Order order) {
        if (dto == null) {
            return Optional.empty();
        }

        Order updatedOrder = order;

        if (dto.getOrderDate() != null) {
            Optional<LocalDate> orderDate = DateParser.parse(dto.getOrderDate());
            if (orderDate.isEmpty()) {
                System.out.println("[Update order dto] DTO validator: Cannot parse order date!");
                return Optional.empty();
            }
            updatedOrder.setOrderDate(orderDate.get());
        }

        if (dto.getDeliveryDate() != null) {
            Optional<LocalDate> deliveryDate = DateParser.parse(dto.getOrderDate());
            if (deliveryDate.isEmpty()) {
                System.out.println("[Update order dto] DTO validator: Cannot parse delivery date!");
                return Optional.empty();
            }
            if (deliveryDate.get().isBefore(updatedOrder.getOrderDate())) {
                System.out.println("[Update order dto] DTO validator: Delivery date must after the order date!");
                return Optional.empty();
            }
            updatedOrder.setDeliveryDate(deliveryDate.get());
        }

        if (dto.getName() != null) {
            updatedOrder.setName(dto.getName());
        }

        if (dto.getAddress() != null) {
            updatedOrder.setAddress(dto.getAddress());
        }

        if (dto.getPhoneNumber() != null) {
            updatedOrder.setPhoneNumber(dto.getPhoneNumber());
        }

        return Optional.of(updatedOrder);
    }
}
