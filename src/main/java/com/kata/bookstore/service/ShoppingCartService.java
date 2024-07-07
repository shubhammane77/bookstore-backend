package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.ShoppingCartItem;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.CreateCartRequest;
import com.kata.bookstore.model.api.CreateCartResponse;
import com.kata.bookstore.model.api.ShoppingCartResponse;
import com.kata.bookstore.model.api.UpdateShoppingCartResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ShoppingCartService {
    Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ShoppingCartResponse getShoppingCart(int userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if(shoppingCart == null){
            return null;
        }
        return modelMapper.map(shoppingCart, ShoppingCartResponse.class);
    }

    @Transactional
    public CreateCartResponse createShoppingCart(CreateCartRequest createCartRequest) {
        CreateCartResponse createCartResponse = new CreateCartResponse();
        try {

            User user = userRepository.findById(createCartRequest.getUserId())
                    .orElseThrow(() -> new InvalidInputException("User not found"));
            ShoppingCart optionalShoppingCart = shoppingCartRepository.findByUserId(createCartRequest.getUserId());
            if (optionalShoppingCart != null) {
                throw new InvalidInputException("Shopping cart already exists");
            }
            ShoppingCart shoppingCart = modelMapper.map(createCartRequest, ShoppingCart.class);
            shoppingCart.getShoppingCartItems().stream().forEach(shoppingCartItem -> shoppingCartItem.setShoppingCart(shoppingCart));
            var result = shoppingCartRepository.save(shoppingCart);
            createCartResponse.setCartId(result.getId());
            return createCartResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception while creating cart... " + ex.getMessage());
            createCartResponse.setErrorMessage(ex.getMessage());
            return createCartResponse;
        } catch (Exception ex) {
            logger.error("Error while creating cart... " + ex.getMessage());
            throw ex;
        }
    }

    public UpdateShoppingCartResponse updateBookQuantity(int cartId, int bookId, int quantity, BigDecimal totalPrice) {
        //
        UpdateShoppingCartResponse updateShoppingCartResponse = new UpdateShoppingCartResponse();
        try {
            ShoppingCart cart = shoppingCartRepository.findById(cartId)
                    .orElseThrow(() -> new InvalidInputException("Cart item not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new InvalidInputException("book not found"));

            Optional<ShoppingCartItem> cartItem = cart.getShoppingCartItems().stream().filter(x -> x.getBook().getId() == bookId).findFirst();

            if (cartItem.isEmpty()) {
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setBookId(bookId);
                shoppingCartItem.setQuantity(quantity);
               shoppingCartItem.setShoppingCart(cart);
                cart.addShoppingCartItem(shoppingCartItem);
                cart.setTotalPrice(totalPrice);
                shoppingCartRepository.save(cart);
                updateShoppingCartResponse.setTotalPrice(cart.getTotalPrice());
                return updateShoppingCartResponse;
            }
            cart.setTotalPrice(totalPrice);
            cart.getShoppingCartItems().stream().filter(x -> x.getBook().getId() == bookId).forEach(x -> x.setQuantity(quantity));
            shoppingCartRepository.save(cart);
            updateShoppingCartResponse.setTotalPrice(cart.getTotalPrice());
            return updateShoppingCartResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception while updating cart" + ex.getMessage());
            updateShoppingCartResponse.setErrorMessage(ex.getMessage());
            return updateShoppingCartResponse;
        } catch (Exception ex) {
            logger.error("Error while creating cart... " + ex.getMessage());
            throw ex;
        }
    }
}
