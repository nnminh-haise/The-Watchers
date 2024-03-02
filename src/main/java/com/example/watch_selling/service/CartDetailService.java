package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.validator.cfg.defs.pl.REGONDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CartDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
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

    public ResponseDto<List<CartDetail>> findAllCartDetail() {
        ResponseDto<List<CartDetail>> response = new ResponseDto<>(null, "", HttpStatus.NOT_FOUND);

        List<CartDetail> cartDetails = cartDetailRepository.findAll();

        if (cartDetails.isEmpty()) {
            response.setMessage("Cannot find any cart detail!");
            return response;
        }

        response.setData(cartDetails);
        response.setMessage("Cart details found successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<CartDetail> findCartDetailById(UUID id) {
        ResponseDto<CartDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id.equals(null)) {
            response.setMessage("Invalid ID!");
            return response;
        }

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) {
            response.setMessage("Cannot find any card detail with the given cart detail ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        response.setData(detail.get());
        response.setMessage("Cart detail found successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<List<CartDetail>> findCartDetailsWithWatchId(UUID watchId) {
        ResponseDto<List<CartDetail>> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (watchId.equals(null)) {
            response.setMessage("Invalid watch ID!");
            return response;
        }

        List<CartDetail> details = cartDetailRepository.findAllDetailWithWatchId(watchId);
        if (details.isEmpty()) {
            response.setMessage("Cannot find any card detail with the given watch ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        response.setData(details);
        response.setMessage("Cart detail found successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<List<CartDetail>> findCartDetailsWithCartId(UUID cartId) {
        ResponseDto<List<CartDetail>> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (cartId.equals(null)) {
            response.setMessage("Invalid cart ID!");
            return response;
        }

        List<CartDetail> details = cartDetailRepository.findAllDetailWithCartId(cartId);
        if (details.isEmpty()) {
            response.setMessage("Cannot find any card detail with the given cart ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        response.setData(details);
        response.setMessage("Cart detail found successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    // TODO: When adding a new watch to the cart, decrease the quantity of the watch inside the watch table and also checking the sending price is equal to the price of the watch
    public ResponseDto<CartDetail> createNewCartDetail(CartDetailDto detail) {
        ResponseDto<CartDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        
        ResponseDto<String> cartDetailValidationResponse = CartDetailDto.validDto(detail);
        if (!cartDetailValidationResponse.getStatus().equals(HttpStatus.OK)) {
            response.setMessage(cartDetailValidationResponse.getMessage());
            response.setStatus(cartDetailValidationResponse.getStatus());
            return response;
        }

        Optional<Cart> cart = cartRepository.findById(detail.getCartId());
        if (!cart.isPresent()) {
            response.setMessage("Cannot find any cart with the given Card ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        Optional<Watch> watch = watchRepository.findById(detail.getWatchId());
        if (!watch.isPresent()) {
            response.setMessage("Cannot find any watch with the given Watch ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        CartDetail newDetail = CartDetailDto.toModel(detail, cart.get(), watch.get());
        cartDetailRepository.save(newDetail);
        // watchRepository.decreaseWatchQuantityById(watch.get().getId(), detail.getQuantity());

        response.setData(newDetail);
        response.setMessage("Cart detail created successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    // TODO: Checking if the price is similar to the price of the watch inside the watch table
    public ResponseDto<CartDetail> updateCartDetailPriceById(UUID id, Double price) {
        ResponseDto<CartDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        if (!CartDetailDto.validPrice(price)) {
            response.setMessage("Invalid price!");
            return response;
        }

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) {
            response.setMessage("Cannot find any cart detail with the given ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        cartDetailRepository.updateCartDetailPriceById(id, price);
        // detail.get().setPrice(price);

        CartDetail updatedDetail = detail.get();
        updatedDetail.setPrice(price);
        response.setData(updatedDetail);
        response.setMessage("Cart detail updated successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<CartDetail> updateCartDetailQuantityById(UUID id, Integer quantity) {
        ResponseDto<CartDetail> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        if (!CartDetailDto.validQuantity(quantity)) {
            response.setMessage("Invalid price!");
            return response;
        }

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) {
            response.setMessage("Cannot find any cart detail with the given ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        cartDetailRepository.updateCartDetailQuantityById(id, quantity);
        // detail.get().setPrice(price);

        CartDetail updatedDetail = detail.get();
        updatedDetail.setQuantity(quantity);
        response.setData(updatedDetail);
        response.setMessage("Cart detail updated successfully!");
        response.setStatus(HttpStatus.OK);
        return response;
    }

    public ResponseDto<String> deleteCartDetailById(UUID id) {
        ResponseDto<String> response = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            response.setMessage("Invalid ID!");
            return response;
        }

        if (!cartDetailRepository.findById(id).isPresent()) {
            response.setMessage("Cannot find any cart detail with the given ID!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        cartDetailRepository.deleteById(id);
        response.setStatus(HttpStatus.OK);
        response.setMessage("Cart detail deleted successfully!");
        return response;
    }
}
