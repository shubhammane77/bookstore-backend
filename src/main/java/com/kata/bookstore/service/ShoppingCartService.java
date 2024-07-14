package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.ShoppingCart;
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
            Book book = bookRepository.findById(createCartRequest.getShoppingCartItems().get(0).getBookId())
                    .orElseThrow(() -> new InvalidInputException("book not found"));



            ShoppingCart shoppingCart = new ShoppingCart(user);
            shoppingCart.addBook(book, 1);
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

            var modifiedCartPrice =  existingCart.addBook(book,quantity);
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
    public UpdateShoppingCartResponse removeCartItem(int cartId, int bookId) throws Exception{
        UpdateShoppingCartResponse updateShoppingCartResponse = new UpdateShoppingCartResponse();
        try {
            ShoppingCart cart = shoppingCartRepository.findById(cartId)
                    .orElseThrow(() -> new InvalidInputException("Cart not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new InvalidInputException("book not found"));

            var modifiedCartPrice = cart.removeBook(book);
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
}
