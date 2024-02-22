package com.example.watch_selling.dtos;

import java.util.UUID;

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
}
