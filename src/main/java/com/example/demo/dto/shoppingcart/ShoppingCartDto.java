package com.example.demo.dto.shoppingcart;

import com.example.demo.dto.cartitem.CartItemsResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemsResponseDto> cartItems;
}
