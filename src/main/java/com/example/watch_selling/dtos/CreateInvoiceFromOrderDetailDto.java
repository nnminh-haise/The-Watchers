package com.example.watch_selling.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceFromOrderDetailDto {
    private UUID orderId;

    private String taxCode;

    private List<UUID> cartDetails;
}
