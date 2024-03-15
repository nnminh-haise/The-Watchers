package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CreateCartDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.model.Watch;
import com.example.watch_selling.repository.CartDetailRepository;
import com.example.watch_selling.repository.CartRepository;
import com.example.watch_selling.repository.WatchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartDetailService {
    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private CartRepository cartRepository;

    public ResponseDto<CartDetail> findCartDetailById(UUID id) {
        ResponseDto<CartDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any card detail with the given cart detail ID!");
        }

        return res
                .setData(detail.get())
                .setMessage("Cart detail found successfully!")
                .setStatus(HttpStatus.OK);
    }

    public ResponseDto<List<CartDetail>> findCartDetailsByCartId(UUID cartId) {
        ResponseDto<List<CartDetail>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (cartId == null) {
            return res.setMessage("Invalid cart ID!");
        }

        try {
            cartDetailRepository.updateAvailabilityByCartIdAndQuantity(cartId);

            List<CartDetail> details = cartDetailRepository.findAllDetailWithCartId(cartId);
            if (details.isEmpty()) {
                return res
                        .setStatus(HttpStatus.NOT_FOUND)
                        .setMessage("Cannot find any card detail with the given cart ID!");
            }

            return res
                    .setData(details)
                    .setStatus(HttpStatus.OK)
                    .setMessage("Cart detail found successfully!");
        } catch (Exception e) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage(e.getMessage());
        }
    }

    public ResponseDto<CartDetail> createNewCartDetail(CreateCartDetailDto detail) {
        ResponseDto<CartDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (detail == null) {
            return res.setMessage("Invalid detail!");
        }

        ResponseDto<String> cartDetailValidationResponse = CreateCartDetailDto.validDto(detail);
        if (!cartDetailValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(cartDetailValidationResponse.getStatus())
                    .setMessage(cartDetailValidationResponse.getMessage());
        }

        Optional<Cart> targetingCart = cartRepository.findById(detail.getCartId());
        if (!targetingCart.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any cart with the given Card ID!");
        }

        Optional<Watch> targetingWatch = watchRepository.findById(detail.getWatchId());
        if (!targetingWatch.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any watch with the given Watch ID!");
        }

        if (detail.getPrice().compareTo(targetingWatch.get().getPrice()) != 0) {
            return res.setMessage("Request price must be equal to the watch price!");
        }

        if (detail.getQuantity() > targetingWatch.get().getQuantity()) {
            return res
                    .setStatus(HttpStatus.CONFLICT)
                    .setMessage("Cannot provide enough Watch!");
        }

        Optional<CartDetail> duplicatedDetail = cartDetailRepository.findByCartIdAndWatchId(
                targetingCart.get().getId(), targetingWatch.get().getId());
        if (duplicatedDetail.isPresent()) {
            return res.setMessage("Duplicated detail!");
        }

        CartDetail newDetail = CreateCartDetailDto.toModel(
                detail, targetingCart.get(), targetingWatch.get());
        newDetail.setAvailable(true);
        try {
            cartDetailRepository.save(newDetail);

            return res
                    .setData(newDetail)
                    .setStatus(HttpStatus.OK)
                    .setMessage("Cart detail created successfully!");
        } catch (Exception e) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage(e.getMessage());
        }
    }

    public ResponseDto<CartDetail> updateCartDetailById(UUID id, UpdateCartDetailDto dto) {
        ResponseDto<CartDetail> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null || dto == null) {
            return res.setMessage("Invalid request!");
        }

        Optional<CartDetail> targetingDetail = cartDetailRepository.findById(id);
        if (!targetingDetail.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any cart detail with the given ID!");
        }

        ResponseDto<UpdateCartDetailDto> dtoValidationResponse = UpdateCartDetailDto.validateDto(dto);
        if (!dtoValidationResponse.getStatus().equals(HttpStatus.OK)) {
            return res
                    .setStatus(dtoValidationResponse.getStatus())
                    .setMessage(dtoValidationResponse.getMessage());
        }

        Optional<Watch> targetingWatch = cartDetailRepository.findWatchByCartDetailId(id);
        if (!targetingWatch.isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find the watch with the given cart detail ID!");
        }

        if (dto.getPrice().compareTo(targetingWatch.get().getPrice()) != 0) {
            return res.setMessage("Request price must be equal to the watch price!");
        }

        if (dto.getQuantity() > targetingWatch.get().getQuantity()) {
            return res
                    .setStatus(HttpStatus.CONFLICT)
                    .setMessage("Cannot provide enough Watch!");
        }

        try {
            cartDetailRepository.updateCartDetailById(id, dto);

            Optional<CartDetail> updatedCartDetail = cartDetailRepository.findById(id);
            return res
                    .setData(updatedCartDetail.get())
                    .setStatus(HttpStatus.OK)
                    .setMessage("Cart detail updated successfully!");
        } catch (Exception e) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage(e.getMessage());
        }
    }

    public ResponseDto<String> deleteCartDetailById(UUID id) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (!cartDetailRepository.findById(id).isPresent()) {
            return res
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setMessage("Cannot find any cart detail with the given ID!");
        }

        try {
            cartDetailRepository.deleteById(id);
            return res
                    .setStatus(HttpStatus.OK)
                    .setMessage("Cart detail deleted successfully!");
        } catch (Exception e) {
            return res
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setMessage(e.getMessage());
        }
    }
}
