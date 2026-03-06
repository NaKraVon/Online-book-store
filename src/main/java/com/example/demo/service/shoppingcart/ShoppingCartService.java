package com.example.demo.service.shoppingcart;

import com.example.demo.dto.cartitem.CreateCartItemsRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemQuantityDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    ShoppingCartDto saveBooksToShoppingCart(CreateCartItemsRequestDto requestDto);

    ShoppingCartDto updateQuantity(UpdateCartItemQuantityDto requestDto, Long cartItemId);

    void deleteById(Long id);

    void addShoppingCartForNewUser(User user);
}
