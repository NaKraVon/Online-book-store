package com.example.demo.service.order.impl;

import com.example.demo.dto.order.OrderItemSummaryDto;
import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemsResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.orderitem.OrderItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUser(user);
        Set<CartItem> cartItem = cartItemRepository
                .getAllCartItemsByShoppingCartId(shoppingCart.getId());
        Order order = setUpNewOrder(orderRequestDto, user);
        OrderItemSummaryDto orderItemSummaryDto = convertCartItemsToOrderItems(shoppingCart,
                order, BigDecimal.ZERO);
        order.setTotal(orderItemSummaryDto.getTotalPrice());
        order.setOrderItems(orderItemSummaryDto.getOrderItems());
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderResponseDto> getByAllOrdersByUserId(Long userId) {
        Set<Order> orders = orderRepository.findAllOrdersByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find order by user id: " + userId));
        return orders.stream().map(orderMapper::toOrderResponseDto).collect(Collectors.toSet());
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
            Long orderId, User user) {
        Order order = orderRepository.findOrderWithIdByUserId(user.getId(), orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find order with id: "
                        + orderId + " and by user id: " + user.getId()));
        order.setStatus(updateOrderStatusRequestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemsResponseDto> getAllOrderItemsInOrder(Long orderId, Long userId) {
        Order order = orderRepository.findOrderWithIdByUserId(userId, orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find order with id: "
                        + orderId + " and by user id: " + userId));
        return order.getOrderItems()
                .stream()
                .map(orderItemMapper::toResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemsResponseDto getOrderItemInOrder(Long orderId, Long orderItemId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can not find order with id: "
                        + orderId + " and by user id: " + userId));
        return orderItemMapper.toResponseDto(orderItemRepository
                .findOrderItemByIdInOrderById(orderId, orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can "
                        + "not find orderItem with id: ")));
    }

    private Order setUpNewOrder(OrderRequestDto orderRequestDto, User user) {
        return orderMapper.toModel(orderRequestDto, user);
    }

    private OrderItemSummaryDto convertCartItemsToOrderItems(ShoppingCart shoppingCart,
                                                             Order order, BigDecimal totalPrice) {
        Set<CartItem> cartItems = cartItemRepository
                .getAllCartItemsByShoppingCartId(shoppingCart.getId());

        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.convertCartItemToOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        return new OrderItemSummaryDto(orderItems, totalPrice);
    }
}
