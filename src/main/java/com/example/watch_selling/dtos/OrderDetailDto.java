package com.example.watch_selling.dtos;

import java.util.UUID;

import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.repository.OrderRepository;
import com.example.watch_selling.repository.WatchRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private UUID id;

    private UUID orderId;

    private UUID watchId;

    private Double price;

    private Integer quantity;

    public static OrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail.equals(null)) {
            return new OrderDetailDto();
        }

        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(orderDetail.getId());
        orderDetailDto.setOrderId(orderDetail.getOrder().getId());
        orderDetailDto.setWatchId(orderDetail.getWatch().getId());
        orderDetailDto.setPrice(orderDetail.getPrice());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        return orderDetailDto;
    }


}
