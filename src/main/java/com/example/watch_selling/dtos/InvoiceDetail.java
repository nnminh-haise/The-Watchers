package com.example.watch_selling.dtos;

import java.util.List;
import java.util.UUID;

import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {
    private UUID id;

    private String invoiceNumber;

    private String createDate;

    private String totalPrice;

    private String totalPriceAfterTax;

    private String taxCode;

    private Order order;

    private List<OrderDetail> orderDetails;

    private String receiverName;

    private String receiverAddress;

    private String receiverPhoneNumber;

    private String orderDate;

    private String estimateDeliveryDate;
}
