package com.example.demo.dto.order;

import com.example.demo.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderItemSummaryDto {
    private Set<OrderItem> orderItems;
    private BigDecimal totalPrice;
}
