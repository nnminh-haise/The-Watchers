package com.example.watch_selling.service;

import java.util.List;
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

    public ResponseDto<List<Cart>> findAllCarts() {
        ResponseDto<List<Cart>> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        List<Cart> carts = cartRepository.findAll();
        if (carts.size() == 0) {
            res.setMessage("Cannot find any carts!");
            return res;
        }

        res.setData(carts);
        res.setMessage("Carts found successfully!");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    public ResponseDto<Cart> updateAccountId(UUID id, UUID newAccountId) {
        ResponseDto<Cart> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            res.setMessage("Invalid ID!");
            return res;
        }
        
        if (newAccountId == null) {
            res.setMessage("Invalid account ID!");
            return res;
        }

        Optional<Account> newAccount = accountRepository.findById(newAccountId);
        if (!newAccount.isPresent()) {
            res.setMessage("Cannot find any account with the given account ID! Invalid account ID!");
            return res;
        }

        Optional<Cart> existingCartWithAccountID = cartRepository.findbyAccountId(newAccountId);
        if (existingCartWithAccountID.isPresent() && !existingCartWithAccountID.get().getId().equals(id)) {
            res.setMessage("Exist cart with the given account ID! Invalid account ID!");
            return res;
        }

        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            res.setMessage("Cannot find any cart with the given ID! Invalid ID!");
            return res;
        }

        cartRepository.updateAccountId(id, newAccountId);

        Cart updatedCart = cart.get();
        updatedCart.setAccount(newAccount.get());
        res.setData(updatedCart);
        res.setMessage("Cart updated successfully!");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        ResponseDto<String> res = new ResponseDto<>(null, "", HttpStatus.BAD_REQUEST);

        if (id == null) {
            res.setMessage("Invalid ID!");
            return res;
        }

        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            res.setMessage("Cannot find any cart with the given ID! Invalid ID!");
            return res;
        }

        cartRepository.updateDeleteStatus(id, status);

        res.setStatus(HttpStatus.OK);
        res.setMessage("Cart updated successfully!");
        return res;
    }
}
