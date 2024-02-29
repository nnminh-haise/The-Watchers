package com.example.watch_selling.dtos;

import java.util.UUID;

import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.model.Watch;
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

    /*
     * Creating a new order detail base on the given OrderDetailDto object.
     * The return OrderDetail object will not have an id and the isDeleted field will use the default value of isDeleted field of OrderDetail object.
     * The return object should be best use for Create new object functionality.
     * @Param: OrderDetailDto object
     * @Return: OrderDetail object
     */
    public static OrderDetail toModel(
        OrderDetailDto dto,
        Order targetingOrder,
        Watch targetingWatch
    ) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(targetingOrder);
        orderDetail.setWatch(targetingWatch);
        orderDetail.setPrice(dto.getPrice());
        orderDetail.setQuantity(dto.getQuantity());
        return orderDetail;
    }
}
