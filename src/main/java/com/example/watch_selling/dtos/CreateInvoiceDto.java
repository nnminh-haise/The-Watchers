package com.example.watch_selling.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceDto {
    private UUID orderId;

    private String taxCode;

    public static Boolean validateDto(CreateInvoiceDto dto) {
        if (dto == null) {
            return false;
        }

        if (dto.getOrderId() == null || dto.getTaxCode() == null) {
            return false;
        }

        if (dto.getTaxCode().isEmpty() || dto.getTaxCode().isBlank()) {
            return false;
        }

        return true;
    }
}
