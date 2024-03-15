package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.service.CartService;

@RestController
@RequestMapping(path = "api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<Cart>> readCartById(@RequestParam("id") UUID id) {
        ResponseDto<Cart> res = cartService.findCartById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<Cart>>> readAllCarts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        ResponseDto<List<Cart>> res = cartService.findAllCarts(page, size);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
