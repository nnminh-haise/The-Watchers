package com.example.watch_selling.dtos;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Cart;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Watch;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateCartDetailDto {
    private UUID watchId;

    private Double price;

    private Integer quantity;

    public static Boolean validID(UUID id) {
        return id != null;
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

    public static ResponseDto<String> validDto(CreateCartDetailDto dto) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!CreateOrderDetailDto.validWatchID(dto.getWatchId())) {
            response.setMessage("Invalid Watch ID!");
            return response;
        }

        if (!CreateOrderDetailDto.validPrice(dto.getPrice())) {
            response.setMessage("Invalid price!");
            return response;
        }

        if (!CreateOrderDetailDto.validQuantity(dto.getQuantity())) {
            response.setMessage("Invalid quantity!");
            return response;
        }

        response.setStatus(HttpStatus.OK);
        return response;
    }

    public static CreateCartDetailDto toDto(CartDetail cartDetail) {
        if (cartDetail == null) {
            return new CreateCartDetailDto();
        }

        CreateCartDetailDto dto = new CreateCartDetailDto();
        dto.setWatchId(cartDetail.getWatch().getId());
        dto.setPrice(cartDetail.getPrice());
        dto.setQuantity(cartDetail.getQuantity());
        return dto;
    }

    /*
     * Creating a new cart detail base on the given CartDetailDto object.
     * The return CartDetail object will not have an id and the isDeleted field will
     * use the default value of isDeleted field of CartDetail object.
     * The return object should be best use for create new object functionality.
     * 
     * @Param: CartDetailDto object
     * 
     * @Return: CartDetail object
     */
    public static CartDetail toModel(
            CreateCartDetailDto dto,
            Cart targetingCart,
            Watch targtingWatch) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(targetingCart);
        cartDetail.setWatch(targtingWatch);
        cartDetail.setPrice(dto.getPrice());
        cartDetail.setQuantity(dto.getQuantity());
        return cartDetail;
    }
}
