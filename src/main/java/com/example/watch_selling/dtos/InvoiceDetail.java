package com.example.watch_selling.dtos;

import java.util.List;

import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {
    private Order order;

    private String invoiceNumber;

    private String createDate;

    private String totalPrice;

    private String totalPriceAfterTax;

    private String taxCode;

    private List<OrderDetail> orderDetails;

    private String receiverName;

    private String receiverAddress;

    private String receiverPhoneNumber;

    private String orderDate;

    private String estimateDeliveryDate;
}
