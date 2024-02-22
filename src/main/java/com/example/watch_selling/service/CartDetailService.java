package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.CartDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.WatchInformationDto;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.model.CartDetail;
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
        return new ResponseDto<>(
            cartDetailRepository.findAll(),
            "Finding process completed!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<CartDetail> findCartDetailById(UUID id) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find any cart detail with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseDto<>(
            detail.get(),
            "Cart detail found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<List<CartDetail>> findCartDetailsWithWatchId(UUID watchId) {
        if (watchId.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        List<CartDetail> details = cartDetailRepository.findAllDetailWithWatchId(watchId);
        if (details.isEmpty()) return new ResponseDto<>(
            null,
            "Cannot find any cart detail with the given watch ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseDto<>(
            details,
            "Cart detail found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<List<CartDetail>> findCartDetailsWithCartId(UUID cartId) {
        if (cartId.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        List<CartDetail> details = cartDetailRepository.findAllDetailWithCartId(cartId);
        if (details.isEmpty()) return new ResponseDto<>(
            null,
            "Cannot find any cart detail with the given cart ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseDto<>(
            details,
            "Cart detail found successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<CartDetail> createNewCartDetail(CartDetailDto detail) {
        if (
            detail.getCartId().equals(null) ||
            detail.getWatchId().equals(null)
        ) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (detail.getPrice() < 0) return new ResponseDto<>(
            null,
            "Price must be at least 0!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (detail.getQuantity() < 0) return new ResponseDto<>(
            null,
            "Quantity must be at least 1!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<Cart> cart = cartRepository.findById(detail.getCartId());
        if (!cart.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find cart with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<WatchInformationDto> watch = watchRepository.findById(detail.getWatchId());
        if (!watch.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find watch with the given ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (cartDetailRepository.findCartDetailWithWatchAndCartId(cart.get().getId(), watch.get().getId()).isPresent()) {
            return new ResponseDto<>(
                null,
                "Cart detail already existed!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        CartDetail newDetail = new CartDetail(
            null,
            cart.get(),
            watch.get().toModel(
                false,
                watchRepository.findWatchTypeByWatchId(watch.get().getId()),
                watchRepository.findWatchBrandByWatchId(watch.get().getId())
            ),
            detail.getPrice(),
            detail.getQuantity()
        );
        cartDetailRepository.save(newDetail);
        watchRepository.decreaseWatchQuantityById(watch.get().getId(), detail.getQuantity());
        return new ResponseDto<>(
            null,
            "New cart detail created successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<CartDetail> updateCartDetailPriceById(UUID id, Double price) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (price < 0) return new ResponseDto<>(
            null,
            "Price must be at least 0!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find cart detail with the given ID! Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        cartDetailRepository.updateCartDetailPriceById(id, price);
        detail.get().setPrice(price);
        return new ResponseDto<>(
            detail.get(),
            "Cart detail updated successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<CartDetail> updateCartDetailQuantityById(UUID id, Integer quantity) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        if (quantity < 0) return new ResponseDto<>(
            null,
            "Quantity must be at least 1!",
            HttpStatus.BAD_REQUEST.value()
        );

        Optional<CartDetail> detail = cartDetailRepository.findById(id);
        if (!detail.isPresent()) return new ResponseDto<>(
            null,
            "Cannot find cart detail with the given ID! Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        cartDetailRepository.updateCartDetailQuantityById(id, quantity);
        detail.get().setQuantity(quantity);
        return new ResponseDto<>(
            detail.get(),
            "Cart detail updated successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> deleteCartDetailById(UUID id) {
        if (id.equals(null)) return new ResponseDto<>(
            null,
            "Invalid ID!",
            HttpStatus.BAD_REQUEST.value()
        );

        cartDetailRepository.deleteById(id);
        return new ResponseDto<>(
            null,
            "Cart detail deleted successfully!",
            HttpStatus.OK.value()
        );
    }
}
