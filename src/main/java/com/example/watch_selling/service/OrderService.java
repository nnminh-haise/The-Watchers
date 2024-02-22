package com.example.watch_selling.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.OrderDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.filters.CustomFilters;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    public OrderService(
        OrderRepository orderRepository,
        AccountRepository accountRepository
    ) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseDto<List<Order>> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) return new ResponseDto<>(
            null,
            "Cannot find any order!",
            HttpStatus.NOT_FOUND.value()
        );

        return new ResponseDto<>(
            orders,
            "Orders found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<Order> createNewOrder(OrderDto newOrderDto) {
        if (newOrderDto.equals(null)) return new ResponseDto<>(
            null,
            "Invalid order!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.idValidation(newOrderDto.getAccountId()) == false) return new ResponseDto<>(
            null,
            "Invalid account ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.nameValidation(newOrderDto.getName()) == false) return new ResponseDto<>(
            null,
            "Invalid name!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.addressValidation(newOrderDto.getAddress()) == false) return new ResponseDto<>(
            null,
            "Invalid name!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.phoneNumberValidation(newOrderDto.getPhoneNumber()) == false) return new ResponseDto<>(
            null,
            "Invalid phone number!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.dateValidation(newOrderDto.getOrderDate()) == false) return new ResponseDto<>(
            null,
            "Invalid order date!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (CustomFilters.dateValidation(newOrderDto.getDeliveryDate()) == false) return new ResponseDto<>(
            null,
            "Invalid delivery date!",
            HttpStatus.BAD_REQUEST.value()
        );

        List<String> statusList = Arrays.asList("Chờ duyệt", "Đang giao hàng", "Hoàn tất", "Đã huỷ");
        Boolean validStatus = false;
        for (String value: statusList) {
            if (newOrderDto.getStatus().equals(value)) {
                validStatus = true;
                break;
            }
        }
        if (!validStatus) return new ResponseDto<>(
            null,
            "Invalid status!",
            HttpStatus.BAD_REQUEST.value()
        );

        @SuppressWarnings("null")
        Optional<Account> associatedAccount = accountRepository.findById(newOrderDto.getAccountId());
        if (!associatedAccount.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find the associated account with the given account ID! Invalid account ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Order> newOrder = newOrderDto.toModel(false, associatedAccount.get());
        if (!newOrder.isPresent()) return new ResponseDto<>(
            null,
            "Error while converting DTO object to model of Order! May cause by invalid date format!",
            HttpStatus.BAD_REQUEST.value()
        );
        if (newOrder.get().getOrderDate().after(newOrder.get().getDeliveryDate())) return new ResponseDto<>(
            null,
            "Delivery date must comes after order date! Invalid order and delivery date!",
            HttpStatus.BAD_REQUEST.value()
        );

        

        Order order = orderRepository.save(newOrder.get());
        return new ResponseDto<>(
            order,
            "Orders found successfully!",
            HttpStatus.OK.value()
        );
    }
}
