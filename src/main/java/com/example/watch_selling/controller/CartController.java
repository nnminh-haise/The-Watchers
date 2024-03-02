package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.service.CartService;

@RestController
@RequestMapping(path = "api/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping("new")
    public ResponseEntity<ResponseDto<Cart>> createNewCart(@RequestBody RequestDto<UUID> accountId) {
        ResponseDto<Cart> response = cartService.createNewCart(accountId.getData());
        if (!response.getStatus().equals(HttpStatus.CREATED.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<Cart>> readCartById(@RequestParam("id") UUID id) {
        ResponseDto<Cart> response = cartService.findCartById(id);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Cart>>> readAllCarts() {
        ResponseDto<List<Cart>> response = cartService.findAllCarts();
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);    
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("update")
    public ResponseEntity<ResponseDto<Cart>> updateCartById(
        @RequestParam("id") UUID id,
        @RequestBody RequestDto<UUID> newAccountId
    ) {
        ResponseDto<Cart> response = cartService.updateAccountId(id, newAccountId.getData());
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> deleteCart(@RequestParam("id") UUID id) {
        ResponseDto<String> response = cartService.updateDeleteStatus(id, true);
        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
