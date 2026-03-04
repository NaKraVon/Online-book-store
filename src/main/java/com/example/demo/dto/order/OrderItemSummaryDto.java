package com.example.demo.dto.order;

import com.example.demo.model.OrderItem;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemSummaryDto {
    private Set<OrderItem> orderItems;
    private BigDecimal totalPrice;
}
