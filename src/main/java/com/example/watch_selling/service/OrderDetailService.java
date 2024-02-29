package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.OrderDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.repository.OrderDetailRepository;
import com.example.watch_selling.repository.OrderRepository;
import com.example.watch_selling.repository.WatchRepository;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WatchRepository watchRepository;

    public ResponseDto<OrderDetail> findById(UUID id) {
        ResponseDto<OrderDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        if (id.equals(null)) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (!orderDetail.isPresent()) {
            response.setMessage("Cannot find any order detail with the given ID!");
            return response;
        }

        response.setData(orderDetail.get());
        response.setMessage("Order detail found successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    public ResponseDto<List<OrderDetail>> findAll() {
        ResponseDto<List<OrderDetail>> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        if (orderDetails.isEmpty()) {
            response.setMessage("Cannot find any order detail!");
            return response;
        }

        response.setData(orderDetails);
        response.setMessage("Order details found successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    // TODO: Handle the unique constraints {Order, Watch}
    public ResponseDto<OrderDetail> createNewOrderDetail(OrderDetailDto orderDetail) {
        ResponseDto<OrderDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        if (orderDetail.equals(null)) {
            response.setMessage("Invalid order detail!");
            return response;
        }

        if (orderDetail.getOrderId() == null) {
            response.setMessage("Invalid order ID!");
            return response;
        }

        if (orderDetail.getWatchId() == null) {
            response.setMessage("Invalid watch ID!");
            return response;
        }

        if (orderDetail.getPrice() < 0) {
            response.setMessage("Price must at least 0!");
            return response;
        }

        if (orderDetail.getQuantity() < 0) {
            response.setMessage("Quantity must at least 0!");
            return response;
        }

        Optional<Order> targetingOrder = orderRepository.findById(orderDetail.getOrderId());
        if (!targetingOrder.isPresent()) {
            response.setMessage("Cannot find any Order with the given Order ID!");
            return response;
        }

        Optional<Watch> targetingWatch = watchRepository.findById(orderDetail.getWatchId());
        if (!targetingWatch.isPresent()) {
            response.setMessage("Cannot find any Watch with the given Watch ID!");
            return response;
        }

        OrderDetail newOrderDetail = OrderDetailDto.toModel(
            orderDetail, targetingOrder.get(), targetingWatch.get()
        );
        orderDetailRepository.save(newOrderDetail);
        response.setData(newOrderDetail);
        response.setMessage("Order detail found successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    public ResponseDto<OrderDetail> updateOrderDetailById(UUID id, OrderDetailDto updateOrderDetail) {
        ResponseDto<OrderDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        Optional<OrderDetail> targetingOrderDetail = orderDetailRepository.findById(id);
        if (!targetingOrderDetail.isPresent()) {
            response.setMessage("Cannot find any order detail with the given ID!");
            return response;
        }
        
        if (!updateOrderDetail.getId().equals(null) && updateOrderDetail.getId().equals(id)) {
            response.setMessage("The given ID inside the in coming update order detail is not identical to the targeting ID! Cannot change ID!");
            return response;
        }

        if (updateOrderDetail.getWatchId().equals(null)) {
            response.setMessage("Invalid watch ID!");
            return response;
        }

        if (updateOrderDetail.getPrice() < 0) {
            response.setMessage("Price must at least 0!");
            return response;
        }

        if (updateOrderDetail.getQuantity() < 0) {
            response.setMessage("Quantity must at least 0!");
            return response;
        }

        Optional<Order> targetingOrder = orderRepository.findById(updateOrderDetail.getOrderId());
        if (!targetingOrder.isPresent()) {
            response.setMessage("Cannot find any Order with the given Order ID!");
            return response;
        }

        Optional<Watch> targetingWatch = watchRepository.findById(updateOrderDetail.getWatchId());
        if (!targetingWatch.isPresent()) {
            response.setMessage("Cannot find any Watch with the given Watch ID!");
            return response;
        }

        OrderDetail updatedOrderDetail = orderDetailRepository.updateOrderDetailById(id, updateOrderDetail);
        
        response.setData(updatedOrderDetail);
        response.setMessage("Order detail updated successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    public ResponseDto<String> updateOrderDetailDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        Optional<OrderDetail> targetingOrderDetail = orderDetailRepository.findById(id);
        if (!targetingOrderDetail.isPresent()) {
            response.setMessage("Cannot find any order detail with the given ID!");
            return response;
        }

        orderDetailRepository.updateOrderDetailDeleteStatusById(id, status);

        response.setMessage("Order detail delete status updated successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }
}
