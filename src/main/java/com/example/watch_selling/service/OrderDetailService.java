package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.OrderDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.OrderDetail;
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

    public ResponseDto<OrderDetailDto> findById(UUID id) {
        ResponseDto<OrderDetailDto> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        if (id.equals(null)) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (!orderDetail.isPresent()) {
            response.setMessage("Cannot find any order detail with the given ID!");
            return response;
        }

        response.setData(OrderDetailDto.toDto(orderDetail.get()));
        response.setMessage("Order detail found successfully!");
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    public ResponseDto<List<OrderDetailDto>> findAll() {
        ResponseDto<List<OrderDetailDto>> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST.value());

        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        if (orderDetails.isEmpty()) {
            response.setMessage("Cannot find any order detail!");
            return response;
        }

        // response.setData(orderDetails);
        return response;
    }
}
