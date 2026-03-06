package com.example.demo.service.order;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemsResponseDto;
import com.example.demo.model.User;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user);

    Page<OrderResponseDto> getByAllOrdersByUserId(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
                                       Long orderId, User user);

    Set<OrderItemsResponseDto> getAllOrderItemsInOrder(Long orderId, Long userId);

    OrderItemsResponseDto getOrderItemInOrder(Long orderId, Long orderItemId, Long userId);
}
