package com.example.demo.service.order.impl;

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
import com.example.demo.repository.cartitem.CartItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.orderitem.OrderItemRepository;
import com.example.demo.repository.shoppingcart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Set<CartItem> cartItems = shoppingCart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot create an order: The shopping cart is empty.");
        }

        Order order = orderMapper.toModel(orderRequestDto, user);

        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.convertCartItemToOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        BigDecimal totalPrice = cartItems.stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotal(totalPrice);
        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponseDto> getByAllOrdersByUserId(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllOrdersByUserId(userId, pageable);
        return orders.map(orderMapper::toOrderResponseDto);
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
}
