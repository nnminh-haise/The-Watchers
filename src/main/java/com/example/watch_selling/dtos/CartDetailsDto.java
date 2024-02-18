package com.example.watch_selling.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsDto {
    private UUID id;

    private UUID accountId;

    private String fullname;

    private Integer itemCount = 0;

    private Boolean isDeleted;
}
