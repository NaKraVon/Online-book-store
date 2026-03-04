package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemsRequestDto {
    @NotNull(message = "book id is required")
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
