package com.kata.bookstore.service;

import com.kata.bookstore.dao.BookRepository;
import com.kata.bookstore.dao.ShoppingCartRepository;
import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.Book;
import com.kata.bookstore.model.ShoppingCart;
import com.kata.bookstore.model.ShoppingCartItem;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.CreateCartItemRequest;
import com.kata.bookstore.model.api.CreateCartRequest;
import com.kata.bookstore.model.api.GetShoppingCartResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTests {
    @Mock
    ShoppingCartRepository shoppingCartRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    ShoppingCartService shoppingCartService;

    private int validBookId;
    private int invalidBookId;
    private int validCartId;
    private int validUserId;
    private int newUserId;
    private int invalidCartId;
    private int invalidUserId;
    private int bookNotInCart;
    private ShoppingCart mockShoppingCart;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        validBookId = 1;
        validCartId = 1;
        validUserId = 1;
        invalidUserId = 0;
        invalidBookId = 0;
        invalidCartId = 0;
        bookNotInCart = 2;
        newUserId = 2;

        Book book = new Book();
        book.setId(validBookId);
        book.setUnitPrice(BigDecimal.TEN);
        book.setTitle("TestBook");

        when(bookRepository.findById(2)).thenReturn(Optional.of(new Book())); // Book not found in cart

        mockShoppingCart = new ShoppingCart();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setBook(book);
        mockShoppingCart.addShoppingCartItem(shoppingCartItem);
        when(shoppingCartRepository.findByUserId(validUserId)).thenReturn(mockShoppingCart);
        User mockUser = new User();

        when(userRepository.findById(validUserId)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(newUserId)).thenReturn(Optional.of(mockUser));

        when(shoppingCartRepository.findByUserId(validUserId)).thenReturn(mockShoppingCart);
        when(shoppingCartRepository.save(any())).thenReturn(new ShoppingCart());
        when(shoppingCartRepository.findByUserId(newUserId)).thenReturn(null);

        when(shoppingCartRepository.findById(validCartId)).thenReturn(Optional.of(mockShoppingCart));
        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(book));
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());
        when(bookRepository.findById(bookNotInCart)).thenReturn(Optional.of(new Book()));


    }

    @Test
    public void createShoppingCart_shouldReturnErrorForInvalidUser() {
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(invalidUserId);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User not found"));
    }

    @Test
    public void createShoppingCart_shouldReturnErrorForExistingUserCart() {
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(validUserId);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Shopping cart already exists"));
    }

    @Test
    public void createShoppingCart_shouldCreateCart() {
        CreateCartRequest createCartRequest = new CreateCartRequest();
        createCartRequest.setUserId(newUserId);
        when(modelMapper.map(createCartRequest, ShoppingCart.class)).thenReturn(mockShoppingCart);
        var result = shoppingCartService.createShoppingCart(createCartRequest);
        assert (result.getErrorMessage() == null);
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidBook() {
        var result = shoppingCartService.updateBookQuantity(validCartId, invalidBookId, 0);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not found"));
    }

    @Test
    public void updateShoppingCart_shouldReturnErrorForInvalidCart() {
        var result = shoppingCartService.updateBookQuantity(invalidCartId, invalidBookId, 0);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Cart not found"));
    }

    @Test
    public void updateShoppingCart_shouldSuccessForValidCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCart.addShoppingCartItem(shoppingCartItem);
        var result = shoppingCartService.updateBookQuantity(validCartId, validBookId, 1);
        assert (result.getErrorMessage() == null);
    }

    @Test
    public void removeCartItem_shouldReturnErrorForNonExistingBook() {
        var result = shoppingCartService.removeCartItem(validCartId, invalidBookId);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not found"));
    }

    @Test
    public void removeCartItem_shouldReturnErrorForShoppingCartDoesNotContainBook() {
        var result = shoppingCartService.removeCartItem(validCartId, bookNotInCart);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("book not present in cart"));
    }

    @Test
    public void removeCartItem_shouldSuccessForExistingCartItem() {
        var result = shoppingCartService.removeCartItem(validCartId, validBookId);
        assert (result.getErrorMessage() == null);
    }

    @Test
    public void delete_shouldReturnErrorForNonExistingCart() {
        var result = shoppingCartService.deleteCart(invalidCartId);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Cart not found"));
    }

    @Test
    public void delete_shouldSuccessForExistingCart() {
        var result = shoppingCartService.deleteCart(validCartId);
        assert (result.getErrorMessage() == null);
    }
}
