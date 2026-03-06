package com.example.demo.controller;

import com.example.demo.dto.cartitem.CreateCartItemsRequestDto;
import com.example.demo.dto.cartitem.UpdateCartItemQuantityDto;
import com.example.demo.dto.shoppingcart.ShoppingCartDto;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Shopping Cart",
        description = "Operations for managing the user's shopping cart and cart items"
)
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Add a book to the shopping cart",
            description = "Adds a specific book with quantity to the authenticated user's cart"
    )
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid CreateCartItemsRequestDto requestDto) {
        return shoppingCartService.saveBooksToShoppingCart(requestDto);
    }

    @Operation(
            summary = "Get the current shopping cart",
            description = "Returns the shopping cart for the currently authenticated user"
    )
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @Operation(
            summary = "Update the quantity of a cart item",
            description = "Updates the quantity of an existing cart item "
                    + "for the authenticated user"
    )
    @PutMapping("/item/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemQuantityDto requestDto) {
        return shoppingCartService.updateQuantity(requestDto, cartItemId);
    }

    @Operation(
            summary = "Delete a cart item",
            description = "Removes a specific item from the authenticated user's shopping cart"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/item/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
