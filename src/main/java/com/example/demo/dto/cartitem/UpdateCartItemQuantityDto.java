package com.example.demo.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemQuantityDto {
    @Positive
    private int quantity;
}
