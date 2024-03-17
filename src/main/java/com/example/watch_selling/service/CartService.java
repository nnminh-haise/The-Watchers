package com.example.watch_selling.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseDto<Cart> createNewCart(UUID accountId) {
        ResponseDto<Cart> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            res.setMessage("Invalid account ID!");
            return res;
        }

        Optional<Account> associatedAccount = accountRepository.findById(accountId);
        if (!associatedAccount.isPresent()) {
            res.setMessage("Cannot find any account with the given account ID");
            return res;
        }

        Cart newCart = cartRepository.save(new Cart(null, associatedAccount.get(), false));

        res.setData(newCart);
        res.setMessage("New cart created successfully!");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    public ResponseDto<Cart> findCartByAccountId(UUID accountId) {
        ResponseDto<Cart> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (accountId == null) {
            return res.setMessage("Invalid account ID!");
        }

        Optional<Cart> cart = cartRepository.findByAccountId(accountId);
        if (!cart.isPresent()) {
            return res.setMessage("Cannot find any cart with the given cart ID!");
        }

        return res
                .setData(cart.get())
                .setStatus(HttpStatus.OK)
                .setMessage("Cart found successfully!");
    }

    public ResponseDto<String> deleteById(UUID id) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (id == null) {
            return res.setMessage("Invalid ID!");
        }

        if (cartRepository.findById(id).isPresent() == false) {
            return res
                    .setMessage("Cannot find any cart with the given ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        cartRepository.deleteById(id);
        return res
                .setStatus(HttpStatus.OK)
                .setMessage("Success!");
    }

    public ResponseDto<String> deleteByAccountId(UUID accountId) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);
        if (accountId == null) {
            return res.setMessage("Invalid account ID!");
        }

        if (cartRepository.findByAccountId(accountId).isPresent() == false) {
            return res
                    .setMessage("Cannot find any cart with the given account ID!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        cartRepository.deleteByAccountId(accountId);
        return res
                .setStatus(HttpStatus.OK)
                .setMessage("Success!");
    }
}
