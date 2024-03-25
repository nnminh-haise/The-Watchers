package com.example.watch_selling.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watch_selling.dtos.CreateCartDetailDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.dtos.UpdateCartDetailDto;
import com.example.watch_selling.model.CartDetail;
import com.example.watch_selling.service.CartDetailService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/cart-detail")
public class CartDetailController {
        @Autowired
        private CartDetailService cartDetailService;

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("/{id}")
        public ResponseEntity<ResponseDto<CartDetail>> readCartDetailById(
                        @PathVariable UUID id) {
                ResponseDto<CartDetail> res = cartDetailService.findCartDetailById(id);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        // TODO: Add pagination
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @GetMapping("all")
        public ResponseEntity<ResponseDto<List<CartDetail>>> readAllCartDetailByCartId() {
                ResponseDto<List<CartDetail>> res = cartDetailService.findCartDetailsByCartId();
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PostMapping("new")
        public ResponseEntity<ResponseDto<CartDetail>> createNewCartDetail(
                        @RequestBody CreateCartDetailDto details) {
                ResponseDto<CartDetail> res = cartDetailService.createNewCartDetail(details);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @PatchMapping("update/{id}")
        public ResponseEntity<ResponseDto<CartDetail>> updateCartDetailById(
                        @PathVariable UUID id,
                        @RequestBody UpdateCartDetailDto dto) {
                ResponseDto<CartDetail> res = cartDetailService.updateCartDetailById(id, dto);
                return ResponseEntity.status(res.getStatus()).body(res);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Request success!"),
                        @ApiResponse(responseCode = "400", description = "Bad request!"),
                        @ApiResponse(responseCode = "500", description = "Internal server error! Server might be down or API was broken!")
        })
        @SuppressWarnings("null")
        @DeleteMapping("delete/{id}")
        public ResponseEntity<ResponseDto<String>> updateCartDetailQuantityById(
                        @PathVariable UUID id) {
                ResponseDto<String> res = cartDetailService.deleteCartDetailById(id);
                return ResponseEntity.status(res.getStatus()).body(res);
        }
}
