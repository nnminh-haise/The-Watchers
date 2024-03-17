package com.example.watch_selling.dtos;

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
public class UpdateOrderDetailDto {
    private Double price;

    private Integer quantity;

    public static Boolean validPrice(Double price) {
        return price >= 0;
    }

    public static Boolean validQuantity(Integer quantity) {
        return quantity >= 0;
    }

    public static ResponseDto<String> validDto(UpdateOrderDetailDto dto) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!UpdateOrderDetailDto.validPrice(dto.getPrice())) {
            response.setMessage("Invalid price!");
            return response;
        }

        if (!UpdateOrderDetailDto.validQuantity(dto.getQuantity())) {
            response.setMessage("Invalid quantity!");
            return response;
        }

        return response;
    }

    public static UpdateOrderDetailDto toDto(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return new UpdateOrderDetailDto();
        }

        UpdateOrderDetailDto orderDetailDto = new UpdateOrderDetailDto();
        orderDetailDto.setPrice(orderDetail.getPrice());
        orderDetailDto.setQuantity(orderDetail.getQuantity());
        return orderDetailDto;
    }

    /*
     * Creating a new order detail base on the given OrderDetailDto object.
     * The return OrderDetail object will not have an id and the isDeleted field
     * will use the default value of isDeleted field of OrderDetail object.
     * The return object should be best use for create new object functionality.
     * 
     * @Param: OrderDetailDto object
     * 
     * @Return: OrderDetail object
     */
    public static OrderDetail toModel(
            UpdateOrderDetailDto dto,
            Order targetingOrder,
            Watch targetingWatch) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(targetingOrder);
        orderDetail.setWatch(targetingWatch);
        orderDetail.setPrice(dto.getPrice());
        orderDetail.setQuantity(dto.getQuantity());
        return orderDetail;
    }
}
