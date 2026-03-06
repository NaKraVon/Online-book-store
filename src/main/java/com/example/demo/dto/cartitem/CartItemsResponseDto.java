package com.example.demo.dto.cartitem;

public record CartItemsResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {
}
