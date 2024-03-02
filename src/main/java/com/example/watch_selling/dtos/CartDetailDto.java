package com.example.watch_selling.dtos;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Cart;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Watch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDto {
    private UUID id;

    private UUID cartId;
    
    private UUID watchId;
    
    private Double price;

    private Integer quantity;

    public static Boolean validID(UUID id) {
        return id != null;
    }

    public static Boolean validCartId(UUID cartId) {
        return cartId != null;
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

    public static ResponseDto<String> validDto(CartDetailDto dto) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!OrderDetailDto.validOrderID(dto.getCartId())) {
            response.setMessage("Invalid Cart ID!");
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

        response.setStatus(HttpStatus.OK);
        return response;
    }

    public static CartDetailDto toDto(CartDetail cartDetail) {
        if (cartDetail == null) {
            return new CartDetailDto();
        }

        CartDetailDto dto = new CartDetailDto();
        dto.setId(cartDetail.getId());
        dto.setCartId(cartDetail.getCart().getId());
        dto.setWatchId(cartDetail.getWatch().getId());
        dto.setPrice(cartDetail.getPrice());
        dto.setQuantity(cartDetail.getQuantity());
        return dto;
    }

    /*
     * Creating a new cart detail base on the given CartDetailDto object.
     * The return CartDetail object will not have an id and the isDeleted field will use the default value of isDeleted field of CartDetail object.
     * The return object should be best use for create new object functionality.
     * @Param: CartDetailDto object
     * @Return: CartDetail object
     */
    public static CartDetail toModel(
        CartDetailDto dto,
        Cart targetingCart,
        Watch targtingWatch
    ) {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setCart(targetingCart);
        cartDetail.setWatch(targtingWatch);
        cartDetail.setPrice(dto.getPrice());
        cartDetail.setQuantity(dto.getQuantity());
        return cartDetail;
    }
}
