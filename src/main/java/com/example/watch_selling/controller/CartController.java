package com.example.watch_selling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.service.CartService;

@RestController
@RequestMapping(path = "api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    @SuppressWarnings("null")
    @GetMapping("my")
    public ResponseEntity<ResponseDto<Cart>> readCartById() {
        Account account = this.getCurrentAccount();
        ResponseDto<Cart> res = cartService.findCartByAccountId(account.getId());
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
