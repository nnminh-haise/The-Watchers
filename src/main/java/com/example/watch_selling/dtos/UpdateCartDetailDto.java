package com.example.watch_selling.dtos;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartDetailDto {
    private Double price;

    private Integer quantity;

    public static ResponseDto<UpdateCartDetailDto> validateDto(UpdateCartDetailDto dto) {
        ResponseDto<UpdateCartDetailDto> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (dto == null || dto.getPrice() < 0 || dto.getQuantity() < 0) {
            return res.setMessage("Invalid request!");
        }

        return res.setStatus(HttpStatus.OK);
    }
}
