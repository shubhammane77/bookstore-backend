package com.kata.bookstore.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        modelMapper.typeMap(CreateCartRequest.class, ShoppingCart.class).addMappings(mapper -> {
//            mapper.skip(ShoppingCart::setId);
//        });
//        modelMapper.typeMap(CreateCartItemRequest.class, ShoppingCartItem.class).addMappings(mapper -> {
//            mapper.skip(ShoppingCartItem::setId);
//        });

        return modelMapper;
    }
}

