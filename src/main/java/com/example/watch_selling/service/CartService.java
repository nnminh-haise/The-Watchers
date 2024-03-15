package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

        Optional<Account> associtatedAccount = accountRepository.findById(accountId);
        if (!associtatedAccount.isPresent()) {
            res.setMessage("Cannot find any account with the given account ID");
            return res;
        }

        Cart newCart = cartRepository.save(new Cart(null, associtatedAccount.get(), false));

        res.setData(newCart);
        res.setMessage("New cart created successfully!");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    public ResponseDto<Cart> findCartById(UUID id) {
        ResponseDto<Cart> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            res.setMessage("Invalid ID!");
            return res;
        }

        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            res.setMessage("Cannot find any cart with the given cart ID!");
            return res;
        }

        res.setData(cart.get());
        res.setMessage("Cart found successfully!");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    public ResponseDto<List<Cart>> findAllCarts(int page, int size) {
        ResponseDto<List<Cart>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<Cart> carts = cartRepository.findAll(PageRequest.of(page, size)).getContent();
        if (carts.size() == 0) {
            return res
                    .setMessage("Cannot find any carts!")
                    .setStatus(HttpStatus.NOT_FOUND);
        }

        return res
                .setData(carts)
                .setMessage("Carts found successfully!")
                .setStatus(HttpStatus.OK);
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

        if (cartRepository.findbyAccountId(accountId).isPresent() == false) {
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
