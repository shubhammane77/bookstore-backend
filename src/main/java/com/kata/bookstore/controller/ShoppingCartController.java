package com.kata.bookstore.controller;

import com.kata.bookstore.model.api.*;
import com.kata.bookstore.service.ShoppingCartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("getShoppingCart")
    public ResponseEntity<ShoppingCartResponse> getShoppingCart(@RequestParam int userId) {
        var result = shoppingCartService.getShoppingCart(userId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("create")
    public ResponseEntity<CreateCartResponse> createShoppingCart(@RequestBody CreateCartRequest createCartRequest) {
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }

    @PostMapping("update")
    public ResponseEntity<UpdateShoppingCartResponse> updateShoppingCart(@RequestBody UpdateShoppingCartRequest updateShoppingCartRequest) {
        var result = shoppingCartService.updateBookQuantity(updateShoppingCartRequest.getCartId(), updateShoppingCartRequest.getBookId(), updateShoppingCartRequest.getQuantity());
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }

    @DeleteMapping("removeCartItem")
    public ResponseEntity<UpdateShoppingCartResponse> removeCartItem(@RequestParam int cartId, int bookId) {
        var result = shoppingCartService.removeCartItem(cartId,bookId);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<UpdateShoppingCartResponse> deleteCart(@RequestParam int cartId) {
        var result = shoppingCartService.deleteCart(cartId);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return new ResponseEntity(result, OK);
    }

//    @DeleteMapping("checkOut")
//    public ResponseEntity<CheckOutResponse> checkOut(@RequestParam int cartId) {
//        var result = shoppingCartService.checkOut(cartId);
//        if (result.getErrorMessage() != null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
//        }
//        return new ResponseEntity(result, OK);
//    }
}
