package com.example.demo.service.shoppingcart.impl;

import com.example.demo.dto.cartitem.CreateCartItemsRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemQuantityDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemsRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id " + requestDto.getBookId())
        );
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItemsRepository.save(cartItem);
        return getShoppingCart();
    }

    @Override
    public ShoppingCartDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id " + cartItemId + " in your shopping cart"));

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemsRepository.save(cartItem);
        return getShoppingCart();
    }

    @Override
    public void deleteById(Long id) {
        ShoppingCart shoppingCart = getShoppingCartByUser();
        CartItem cartItemToRemove = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id " + id + " in your shopping cart"));

        cartItemsRepository.delete(cartItemToRemove);
    }

    @Override
    public void addShoppingCartForNewUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())
        ) {
            throw new RuntimeException("User is not authenticated");
        }

        User currentUser = (User) authentication.getPrincipal();
        return shoppingCartRepository.findShoppingCartByUser(currentUser);
    }
}
