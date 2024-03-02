package com.example.watch_selling.dtos;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.example.watch_selling.model.Invoice;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceUpdateDto {
    private String createDate;

    private Double total;

    private String taxcode;

    private UUID orderId;

    public static Boolean validCreateDate(String createDate) {
        Optional<Date> d = OrderDto.parseDate(createDate);
        return d.isPresent();
    }

    public static Boolean validTotal(Double total) {
        return total >= 0;
    }

    public static Boolean validTaxCode(String taxCode) {
        return taxCode != null && !taxCode.isBlank() && !taxCode.isEmpty();
    }

    public static Boolean validOrderId(UUID id) {
        return id != null;
    }

    public static ResponseDto<InvoiceUpdateDto> validDto(InvoiceUpdateDto dto) {
        ResponseDto<InvoiceUpdateDto> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (dto == null) {
            return res.setMessage("Invalid DTO!");
        }

        if (!InvoiceUpdateDto.validCreateDate(dto.getCreateDate())) {
            return res.setMessage("Invalid create date!");
        }

        if (!InvoiceUpdateDto.validTotal(dto.getTotal())) {
            return res.setMessage("Invalid total!");
        }
        
        if (!InvoiceUpdateDto.validTaxCode(dto.getTaxcode())) {
            return res.setMessage("Invalid tax code!");
        }
        
        if (!InvoiceUpdateDto.validOrderId(dto.getOrderId())) {
            return res.setMessage("Invalid order ID!");
        }

        return res
            .setStatus(HttpStatus.OK)
            .setMessage("Valid!")
            .setData(dto);
    }

    public static Optional<Invoice> toModel(
        InvoiceUpdateDto dto,
        Boolean deleteStatus,
        Order order
    ) {
        ResponseDto<InvoiceUpdateDto> dtoValidationResponse = InvoiceUpdateDto.validDto(dto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return Optional.empty();
        }

        Invoice i = new Invoice();
        i.setCreateDate(OrderDto.parseDate(dto.getCreateDate()).get());
        i.setTotal(dto.getTotal());
        i.setTaxCode(dto.getTaxcode());
        i.setOrder(order);
        i.setDeleteStatus(deleteStatus);

        return Optional.of(i);
    }
}
