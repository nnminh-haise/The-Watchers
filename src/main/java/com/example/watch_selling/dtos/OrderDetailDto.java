package com.example.watch_selling.dtos;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.model.Watch;

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

    public static Boolean validID(UUID id) {
        return id != null;
    }

    public static Boolean validOrderID(UUID orderId) {
        return orderId != null;
    }

    public static Boolean validWatchID(UUID watchId) {
        return watchId != null;
    }

    public static Boolean validPrice(Double price) {
        return price >= 0;
    }

    public static Boolean validQuantity(Integer quantity) {
        return quantity >= 0;
    }

    public static ResponseDto<String> validDto(OrderDetailDto dto) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!OrderDetailDto.validOrderID(dto.getOrderId())) {
            response.setMessage("Invalid Order ID!");
            return response;
        }

        if (!OrderDetailDto.validWatchID(dto.getWatchId())) {
            response.setMessage("Invalid Watch ID!");
            return response;
        }

        if (!OrderDetailDto.validPrice(dto.getPrice())) {
            response.setMessage("Invalid price!");
            return response;
        }

        if (!OrderDetailDto.validQuantity(dto.getQuantity())) {
            response.setMessage("Invalid quantity!");
            return response;
        }

        return response;
    }

    public static OrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail ==null) {
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
     * The return object should be best use for create new object functionality.
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
