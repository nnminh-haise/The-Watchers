package com.example.watch_selling.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateInvoiceDto;
import com.example.watch_selling.dtos.CreateOrderDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.model.enums.OrderStatus;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.OrderDetailRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CartDetailService cartDetailService;

    public ResponseDto<List<Order>> findAllOrders(
            UUID accountId, int page, int size, String sortBy, String fromOrderDate, String toOrderDate) {
        ResponseDto<List<Order>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (!sortBy.equalsIgnoreCase("asc") && !sortBy.equalsIgnoreCase("desc")) {
            return res.setMessage("Invalid sort by value!");
        }

        try {
            LocalDate validFromOrderDate = null;
            LocalDate validToOrderDate = null;
            if (fromOrderDate != null)
                validFromOrderDate = LocalDate.parse(fromOrderDate);
            if (toOrderDate != null)
                validToOrderDate = LocalDate.parse(toOrderDate);

            Pageable selectingPage = PageRequest.of(page, size);
            List<Order> orders = sortBy.equalsIgnoreCase("ASC")
                    ? orderRepository.findOrdersByAccountIdAndOrderDateASC(
                            accountId, validFromOrderDate, validToOrderDate, selectingPage)
                    : orderRepository.findOrdersByAccountIdAndOrderDateDESC(
                            accountId, validFromOrderDate, validToOrderDate, selectingPage);
            if (orders.isEmpty())
                return res
                        .setStatus(HttpStatus.NO_CONTENT)
                        .setMessage("Cannot find any order!");

            return res
                    .setMessage("Orders found successfully!")
                    .setStatus(HttpStatus.OK)
                    .setData(orders);
        } catch (Exception e) {
            return res.setMessage("Invalid order date!");
        }
    }

    public ResponseDto<Order> findById(UUID id) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null)
            return res.setMessage("Invalid ID!");

        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent())
            return res.setMessage("Cannot find order with the given ID!");

        return res
                .setMessage("Order found successfully!")
                .setStatus(HttpStatus.OK)
                .setData(order.get());
    }

    public ResponseDto<Order> createNewOrder(CreateOrderDto payload, UUID accountId) {
        ResponseDto<Order> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<Account> customerAccount = accountRepository.findById(accountId);
        if (!customerAccount.isPresent()) {
            return res.setMessage("Cannot find the associated account with the given account ID!");
        }

        LocalDate orderDate = LocalDate.now();
        LocalDate estimateDeliveryDate = orderDate.plusDays(3);

        Order newOrder = new Order();
        newOrder.setAccount(customerAccount.get());
        newOrder.setAddress(payload.getAddress());
        newOrder.setOrderDate(orderDate);
        newOrder.setDeliveryDate(estimateDeliveryDate);
        newOrder.setName(payload.getName());
        newOrder.setPhoneNumber(payload.getPhoneNumber());
        newOrder.setStatus(OrderStatus.PENDING.toString());
        newOrder.setIsDeleted(false);

        this.orderRepository.save(newOrder);

        for (UUID cartDetailId : payload.getOrderDetails()) {
            ResponseDto<CartDetail> detail = this.cartDetailService.findCartDetailById(cartDetailId);

            OrderDetail newOrderDetail = new OrderDetail();
            newOrderDetail.setOrder(newOrder);
            newOrderDetail.setWatch(detail.getData().getWatch());
            newOrderDetail.setPrice(detail.getData().getPrice());
            newOrderDetail.setQuantity(detail.getData().getQuantity());

            this.orderDetailRepository.save(newOrderDetail);

            this.cartDetailService.deleteCartDetailById(cartDetailId);
        }

        CreateInvoiceDto invoicePayload = new CreateInvoiceDto();
        invoicePayload.setOrderId(newOrder.getId());
        invoicePayload.setTaxCode(payload.getTaxCode());
        this.invoiceService.createNewInvoice(invoicePayload);

        return res
                .setMessage("New order created successfully!")
                .setStatus(HttpStatus.OK)
                .setData(newOrder);
    }
}
