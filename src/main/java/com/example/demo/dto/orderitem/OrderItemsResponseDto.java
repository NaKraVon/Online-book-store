package com.example.demo.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
