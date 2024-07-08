package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PlaceOrderResponse implements Serializable {
    private String ErrorMessage;
}
