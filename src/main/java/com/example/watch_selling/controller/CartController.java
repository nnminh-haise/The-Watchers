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

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(path = "api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Account) authentication.getPrincipal();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request success!"),
            @ApiResponse(responseCode = "400", description = "Bad request!"),
            @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
    })
    @SuppressWarnings("null")
    @GetMapping("my")
    public ResponseEntity<ResponseDto<Cart>> readCartById() {
        Account account = this.getCurrentAccount();
        ResponseDto<Cart> res = cartService.findCartByAccountId(account.getId());
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
