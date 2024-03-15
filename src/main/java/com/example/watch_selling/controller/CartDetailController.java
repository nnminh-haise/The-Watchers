package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateCartDetailDto;
import com.example.watch_selling.dtos.RequestDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateCartDetailDto;
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

    @SuppressWarnings("null")
    @GetMapping("")
    public ResponseEntity<ResponseDto<CartDetail>> readCartDetailById(
            @RequestParam("id") UUID id) {
        ResponseDto<CartDetail> res = cartDetailService.findCartDetailById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @GetMapping("all")
    public ResponseEntity<ResponseDto<List<CartDetail>>> readAllCartDetailByCartId(
            @RequestParam("cart_id") UUID cartId) {
        ResponseDto<List<CartDetail>> res = cartDetailService.findCartDetailsByCartId(cartId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PutMapping("new")
    public ResponseEntity<ResponseDto<CartDetail>> createNewCartDetail(
            @RequestBody RequestDto<CreateCartDetailDto> details) {
        ResponseDto<CartDetail> res = cartDetailService.createNewCartDetail(details.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @PatchMapping("update")
    public ResponseEntity<ResponseDto<CartDetail>> updateCartDetailById(
            @RequestParam("id") UUID id,
            @RequestBody RequestDto<UpdateCartDetailDto> dto) {
        ResponseDto<CartDetail> res = cartDetailService.updateCartDetailById(id, dto.getData());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @SuppressWarnings("null")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto<String>> updateCartDetailQuantityById(
            @RequestParam("id") UUID id) {
        ResponseDto<String> res = cartDetailService.deleteCartDetailById(id);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
