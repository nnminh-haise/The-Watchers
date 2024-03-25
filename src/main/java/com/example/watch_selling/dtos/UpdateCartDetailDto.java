package com.example.watch_selling.dtos;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
