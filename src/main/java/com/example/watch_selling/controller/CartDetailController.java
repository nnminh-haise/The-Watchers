package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CartDetailDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.service.CartDetailService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/cart/detail")
public class CartDetailController {
    @Autowired
    private CartDetailService cartDetailService;

    @GetMapping("")
    public ResponseEntity<ResponseDto<CartDetail>> getCartDetailByID(@RequestParam("id") UUID id) {
        ResponseDto<CartDetail> response = cartDetailService.findCartDetailById(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("watch")
    public ResponseEntity<ResponseDto<List<CartDetail>>> getAllCartDetailWithWatchId(@RequestParam("id") UUID id) {
        ResponseDto<List<CartDetail>> response = cartDetailService.findCartDetailsWithWatchId(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("cart")
    public ResponseEntity<ResponseDto<List<CartDetail>>> getAllCartDetailWithCartId(@RequestParam("id") UUID id) {
        ResponseDto<List<CartDetail>> response = cartDetailService.findCartDetailsWithCartId(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<CartDetail>>> getAllCartDetail() {
        ResponseDto<List<CartDetail>> response = cartDetailService.findAllCartDetail();
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<CartDetail>> createNewCartDetail(@RequestBody RequestDto<CartDetailDto> details) {
        if (details.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                null,
                "Received a null body from request!",
                HttpStatus.BAD_REQUEST.value()
            ));
        }
        ResponseDto<CartDetail> response = cartDetailService.createNewCartDetail(details.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("update/price")
    public ResponseEntity<ResponseDto<CartDetail>> updateCartDetailPriceById(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<Double> price
    ) {
        System.out.println("LOG: " + price.getData());
        ResponseDto<CartDetail> response = cartDetailService.updateCartDetailPriceById(id, price.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("update/quantity")
    public ResponseEntity<ResponseDto<CartDetail>> updateCartDetailQuantityById(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<Integer> quantity
    ) {
        ResponseDto<CartDetail> response = cartDetailService.updateCartDetailQuantityById(id, quantity.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> updateCartDetailQuantityById(@RequestParam("id") UUID id) {
        ResponseDto<String> response = cartDetailService.deleteCartDetailById(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
