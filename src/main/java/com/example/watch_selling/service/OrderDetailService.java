package com.example.watch_selling.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateOrderDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Order;
import com.example.watch_selling.model.OrderDetail;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.repository.CartDetailRepository;
import com.example.watch_selling.repository.OrderDetailRepository;
import com.example.watch_selling.repository.OrderRepository;
import com.example.watch_selling.repository.WatchRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WatchRepository watchRepository;

    public ResponseDto<OrderDetail> findById(UUID id) {
        ResponseDto<OrderDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            return response.setMessage("Invalid ID!");
        }

        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if (!orderDetail.isPresent()) {
            return response.setMessage("Cannot find any order detail with the given ID!");
        }

        return response
                .setData(orderDetail.get())
                .setMessage("Order detail found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<List<OrderDetail>> findAllOrderDetailByOrderId(UUID orderId, int page, int size, String sortBy) {
        ResponseDto<List<OrderDetail>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (orderId == null) {
            return res.setMessage("Invalid order ID!");
        }

        if (!sortBy.equalsIgnoreCase("asc") || !sortBy.equalsIgnoreCase("desc")) {
            return res.setMessage("Invalid sort by!");
        }

        Pageable paging = PageRequest.of(page, size);
        List<OrderDetail> orderDetails = sortBy.equalsIgnoreCase("asc")
                ? orderDetailRepository.findAllOrderDetailByOrderIdASC(orderId, paging).getContent()
                : orderDetailRepository.findAllOrderDetailByOrderIdASC(orderId, paging).getContent();
        if (orderDetails.isEmpty()) {
            return res
                    .setStatus(HttpStatus.NO_CONTENT)
                    .setMessage("Cannot find any order detail!");
        }

        return res
                .setData(orderDetails)
                .setStatus(HttpStatus.OK)
                .setMessage("Order details found successfully!");
    }

    @Modifying
    @Transactional
    private Optional<OrderDetail> createOrderDetailAndUpdateNewInStockQuantity(
            OrderDetail newOrderDetail, UUID targetingWatch, Integer newInStockQuantity) {

        int updatedWatch = watchRepository.updateWatchQuantityById(targetingWatch, newInStockQuantity);
        if (updatedWatch == 0) {
            return Optional.empty();
        }
        return Optional.of(orderDetailRepository.save(newOrderDetail));
    }

    @Modifying
    @Transactional
    public ResponseDto<OrderDetail> createNewOrderDetail(CreateOrderDetailDto createOrderDetailDto) {
        ResponseDto<OrderDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (createOrderDetailDto == null) {
            return res.setMessage("Invalid order detail!");
        }

        ResponseDto<String> dtoValidationResponse = CreateOrderDetailDto.validDto(createOrderDetailDto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(dtoValidationResponse.getStatus())
                    .setMessage(dtoValidationResponse.getMessage());
        }

        Optional<Order> targetingOrder = orderRepository.findById(createOrderDetailDto.getOrderId());
        if (!targetingOrder.isPresent()) {
            return res.setMessage("Cannot find any Order with the given Order ID!");
        }

        Optional<Watch> targetingWatch = watchRepository.findById(createOrderDetailDto.getWatchId());
        if (!targetingWatch.isPresent()) {
            return res.setMessage("Cannot find any Watch with the given Watch ID!");
        }

        if (targetingWatch.get().getPrice().compareTo(createOrderDetailDto.getPrice()) != 0) {
            return res.setMessage("Requesting price must equal to the one in stock!");
        }

        if (targetingWatch.get().getQuantity() < createOrderDetailDto.getQuantity()) {
            return res.setMessage("Requesting quantity must less then or equal to the one in stock!");
        }

        OrderDetail newOrderDetail = CreateOrderDetailDto.toModel(
                createOrderDetailDto, targetingOrder.get(), targetingWatch.get());

        int requestingQuantity = createOrderDetailDto.getQuantity();
        int newInStockQuantity = targetingWatch.get().getQuantity() - requestingQuantity;
        this.createOrderDetailAndUpdateNewInStockQuantity(
                newOrderDetail, targetingWatch.get().getId(), newInStockQuantity);
        return res
                .setData(newOrderDetail)
                .setStatus(HttpStatus.OK)
                .setMessage("Order detail found successfully!");
    }

    @Modifying
    @Transactional
    public ResponseDto<List<OrderDetail>> createOrderDetailsFromCartDetailsOfOrder(
            UUID orderId, List<UUID> cartDetailIds) {
        ResponseDto<List<OrderDetail>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (orderId == null) {
            return res.setMessage("Invalid order ID!");
        }

        if (cartDetailIds.isEmpty()) {
            return res.setMessage("No cart detail provided!");
        }

        Optional<Order> targetingOrder = orderRepository.findById(orderId);
        if (!targetingOrder.isPresent()) {
            return res.setMessage("Cannot find any order with the given order ID!");
        }

        List<OrderDetail> newOrderDetails = new ArrayList<>();
        for (UUID cartDetailId : cartDetailIds) {
            Optional<CartDetail> cartDetail = cartDetailRepository.findById(cartDetailId);
            if (!cartDetail.isPresent()) {
                return res.setMessage("Invalid cart detail ID!");
            }
            Watch targetingWatch = cartDetail.get().getWatch();
            OrderDetail newOrderDetail = new OrderDetail(
                    null,
                    targetingOrder.get(),
                    targetingWatch,
                    cartDetail.get().getPrice(),
                    cartDetail.get().getQuantity(),
                    false);

            int requestingQuantity = cartDetail.get().getQuantity();
            int newInStockQuantity = targetingWatch.getQuantity() - requestingQuantity;
            this.createOrderDetailAndUpdateNewInStockQuantity(
                    newOrderDetail, targetingWatch.getId(), newInStockQuantity);
            newOrderDetails.add(newOrderDetail);
        }

        return res
                .setData(newOrderDetails)
                .setStatus(HttpStatus.OK)
                .setMessage("Success!");
    }

    @Modifying
    @Transactional
    private Integer updateOrderDetailAndWatchInStockQuantity(
            UUID id, UpdateCartDetailDto updateCartDetailDto, UUID watchId, Integer newInStockQuantity) {

        Integer updatedWatch = watchRepository.updateWatchQuantityById(watchId, newInStockQuantity);
        if (updatedWatch == 0) {
            return 0;
        }
        return orderDetailRepository.updateOrderDetailById(id, updateCartDetailDto);
    }

    @Modifying
    @Transactional
    public ResponseDto<OrderDetail> updateOrderDetailById(UUID id, UpdateCartDetailDto updateCartDetailDto) {
        ResponseDto<OrderDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<OrderDetail> targetingOrderDetail = orderDetailRepository.findById(id);
        if (!targetingOrderDetail.isPresent()) {
            return res.setMessage("Cannot find any order detail with the given ID!");
        }

        ResponseDto<UpdateCartDetailDto> dtoValidationResponse = UpdateCartDetailDto.validateDto(updateCartDetailDto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(dtoValidationResponse.getStatus())
                    .setMessage(dtoValidationResponse.getMessage());
        }

        Watch targetingWatch = targetingOrderDetail.get().getWatch();
        if (updateCartDetailDto.getPrice().compareTo(targetingWatch.getPrice()) != 0) {
            return res.setMessage("Requesting price must be the same to the one in stock!");
        }

        if (updateCartDetailDto.getQuantity() > targetingWatch.getQuantity()) {
            return res.setMessage("Requesting quantity must be less than or equal to the one in stock!");
        }

        int currentRequestQuantity = targetingOrderDetail.get().getQuantity();
        int requestingQuantity = updateCartDetailDto.getQuantity();
        int newInStockQuantity = targetingWatch.getQuantity() + (currentRequestQuantity - requestingQuantity);
        if (this.updateOrderDetailAndWatchInStockQuantity(
                id, updateCartDetailDto, targetingWatch.getId(), newInStockQuantity) == 0) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage("Something wrong!");
        }

        Optional<OrderDetail> newDetail = orderDetailRepository.findById(id);
        return res
                .setData(newDetail.get())
                .setStatus(HttpStatus.OK)
                .setMessage("Order detail updated successfully!");
    }

    @Modifying
    @Transactional
    public ResponseDto<String> updateOrderDetailDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        Optional<OrderDetail> targetingOrderDetail = orderDetailRepository.findById(id);
        if (!targetingOrderDetail.isPresent()) {
            response.setMessage("Cannot find any order detail with the given ID!");
            return response;
        }

        orderDetailRepository.updateOrderDetailDeleteStatusById(id, status);

        response.setMessage("Order detail delete status updated successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

}
