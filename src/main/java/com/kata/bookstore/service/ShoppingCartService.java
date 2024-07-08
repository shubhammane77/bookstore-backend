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
import com.kata.bookstore.model.api.GetShoppingCartResponse;
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

    /* Returns existing cart of user */
    public GetShoppingCartResponse getShoppingCart(int userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            return null;
        }
        return modelMapper.map(shoppingCart, GetShoppingCartResponse.class);
    }

    /* Creates new cart for user, returns cartId */
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
            shoppingCart.setUser(user);

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

    /* Update book quantity of the cart item, calculates new price */
    public UpdateShoppingCartResponse updateBookQuantity(int cartId, int bookId, int quantity) {
        //
        UpdateShoppingCartResponse updateShoppingCartResponse = new UpdateShoppingCartResponse();
        try {
            ShoppingCart existingCart = shoppingCartRepository.findById(cartId)
                    .orElseThrow(() -> new InvalidInputException("Cart not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new InvalidInputException("book not found"));

            // Calculate new price
            var modifiedCartPrice = calculateModifiedCartPrice(existingCart, book, quantity);

            //if book is existing, then update the quantity, if not create new cart item
            Optional<ShoppingCartItem> existingBookItem = existingCart.getShoppingCartItems().stream()
                    .filter(x -> x.getBook().getId() == bookId)
                    .findFirst();

            if (existingBookItem.isEmpty()) {
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem(existingCart, book, quantity);
                existingCart.addShoppingCartItem(shoppingCartItem);
                existingCart.setTotalPrice(modifiedCartPrice);
                shoppingCartRepository.save(existingCart);

                updateShoppingCartResponse.setTotalPrice(existingCart.getTotalPrice());
                return updateShoppingCartResponse;
            }
            existingCart.setTotalPrice(modifiedCartPrice);
            existingCart.getShoppingCartItems().stream()
                    .filter(x -> x.getBook().getId() == bookId)
                    .forEach(x -> x.setQuantity(quantity));

            shoppingCartRepository.save(existingCart);
            updateShoppingCartResponse.setTotalPrice(modifiedCartPrice);
            return updateShoppingCartResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception while updating cart " + ex.getMessage());
            updateShoppingCartResponse.setErrorMessage(ex.getMessage());
            return updateShoppingCartResponse;
        } catch (Exception ex) {
            logger.error("Error while updating cart " + ex.getMessage());
            throw ex;
        }
    }

    /* Remove book from cart items table */
    public UpdateShoppingCartResponse removeCartItem(int cartId, int bookId) {
        UpdateShoppingCartResponse updateShoppingCartResponse = new UpdateShoppingCartResponse();
        try {
            ShoppingCart cart = shoppingCartRepository.findById(cartId)
                    .orElseThrow(() -> new InvalidInputException("Cart not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new InvalidInputException("book not found"));


            //existing book check
            Optional<ShoppingCartItem> cartItemToBeRemoved = cart.getShoppingCartItems().stream()
                    .filter(x -> x.getBook().getId() == bookId)
                    .findFirst();

            if (cartItemToBeRemoved.isEmpty()) {
                throw new InvalidInputException("book not present in cart");
            }

            cart.getShoppingCartItems().remove(cartItemToBeRemoved.get());
            var modifiedCartPrice = calculateModifiedCartPrice(cart, book, 0);
            cart.setTotalPrice(modifiedCartPrice);
            shoppingCartRepository.save(cart);

            updateShoppingCartResponse.setTotalPrice(modifiedCartPrice);
            return updateShoppingCartResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception while removing cart item " + ex.getMessage());
            updateShoppingCartResponse.setErrorMessage(ex.getMessage());
            return updateShoppingCartResponse;
        } catch (Exception ex) {
            logger.error("Error while removing cart item " + ex.getMessage());
            throw ex;
        }
    }

    /* Deletes cart */
    public UpdateShoppingCartResponse deleteCart(int cartId) {
        UpdateShoppingCartResponse updateShoppingCartResponse = new UpdateShoppingCartResponse();
        try {
            ShoppingCart cart = shoppingCartRepository.findById(cartId)
                    .orElseThrow(() -> new InvalidInputException("Cart not found"));

            shoppingCartRepository.delete(cart);
            return updateShoppingCartResponse;
        } catch (InvalidInputException ex) {
            logger.error("Exception while deleting cart" + ex.getMessage());
            updateShoppingCartResponse.setErrorMessage(ex.getMessage());
            return updateShoppingCartResponse;
        } catch (Exception ex) {
            logger.error("Error while deleting cart... " + ex.getMessage());
            throw ex;
        }
    }


    private BigDecimal calculateModifiedCartPrice(ShoppingCart cart, Book book, int quantity) {
        BigDecimal cartPriceWithoutBook = BigDecimal.ZERO;

        for (int i = 0; i < cart.getShoppingCartItems().size(); i++) {
            var currentItem = cart.getShoppingCartItems().get(i);

            if (currentItem.getBook().getId() != book.getId()) {
                cartPriceWithoutBook = cartPriceWithoutBook
                        .add(BigDecimal.valueOf(currentItem.getQuantity()).multiply(currentItem.getBook().getUnitPrice()));
            }
        }

        BigDecimal bookPrice = book.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        return cartPriceWithoutBook.add(bookPrice);
    }
}
