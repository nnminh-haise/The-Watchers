package com.example.watch_selling.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Cart;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.CartRepository;

@Service
public class CartService {
    private CartRepository cartRepository;

    private AccountRepository accountRepository;

    public CartService(CartRepository cartRepository, AccountRepository accountRepository) {
        this.cartRepository = cartRepository;
        this.accountRepository = accountRepository;
    }

    public ResponseDto<Cart> createNewCart(UUID accountId) {
        if (accountId.equals(null)) {
            return new ResponseDto<>(
                null,
                "Account ID cannot null! Invalid account ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (cartRepository.findbyAccountId(accountId).isPresent()) {
            return new ResponseDto<>(
                null,
                "The given account already had a shopping cart! Invalid account ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Cart newCart = cartRepository.save(new Cart(null, accountRepository.findById(accountId).get(), false));

        return new ResponseDto<>(
            newCart,
            "New cart created successfully!",
            HttpStatus.CREATED.value()
        );
    }

    public ResponseDto<Cart> findCartById(UUID id) {
        if (id.equals(null)) {
            return new ResponseDto<>(
                null,
                "Cart ID cannot null! Invalid cart ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find cart with the given ID!",
                HttpStatus.NOT_FOUND.value()
            );    
        }

        return new ResponseDto<>(
            cart.get(),
            "Cart founded successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<List<Cart>> findAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.size() == 0) {
            return new ResponseDto<>(
                null,
                "Cannot find any carts!",
                HttpStatus.NOT_FOUND.value()
            );    
        }

        return new ResponseDto<>(
            carts,
            "Carts founded successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> updateAccountId(UUID id, UUID newAccountId) {
        if (id.equals(null) || newAccountId.equals(null)) {
            return new ResponseDto<>(
                null,
                "Invalid request!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<Account> account = accountRepository.findById(newAccountId);
        if (!account.isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find account with the given ID! Invalid account ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<Cart> existingCartWithAccountID = cartRepository.findbyAccountId(newAccountId);
        if (!existingCartWithAccountID.isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find cart with the given ID! Invalid ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }
        else if (!existingCartWithAccountID.get().getId().equals(id)) {
            return new ResponseDto<>(
                null,
                "Exist cart with the given account ID! Invalid account ID!",
                HttpStatus.BAD_REQUEST.value()
            );            
        }

        // * Bad example for if statement
        // if (existingCartWithAccountID.isPresent() && !existingCartWithAccountID.get().getId().equals(id)) {
        //     return new ResponseDto<>(
        //         null,
        //         "Exist cart with the given account ID! Invalid account ID!",
        //         HttpStatus.BAD_REQUEST.value()
        //     );
        // }
        // else if (!existingCartWithAccountID.isPresent()) {
        //     return new ResponseDto<>(
        //         null,
        //         "Cannot find cart with the given ID! Invalid ID!",
        //         HttpStatus.BAD_REQUEST.value()
        //     );
        // }

        cartRepository.updateAccountId(id, newAccountId);
        return new ResponseDto<>(
            null,
            "Cart updated successfully!",
            HttpStatus.OK.value()
        );
    }

    public ResponseDto<String> updateDeleteStatus(UUID id, Boolean status) {
        if (id.equals(null)) {
            return new ResponseDto<>(
                null,
                "Invalid ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        Optional<Cart> cart = cartRepository.findById(id);
        if (!cart.isPresent()) {
            return new ResponseDto<>(
                null,
                "Cannot find cart with the given ID! Invalid ID!",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        cartRepository.updateDeleteStatus(id, status);
        return new ResponseDto<>(
            null,
            "Cart updated successfully!",
            HttpStatus.OK.value()
        );
    }
}
