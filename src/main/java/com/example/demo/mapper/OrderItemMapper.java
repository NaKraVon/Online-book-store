package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.orderitem.OrderItemsResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.price", target = "price")
    @Mapping(target = "id", ignore = true)
    OrderItem convertCartItemToOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemsResponseDto toResponseDto(OrderItem orderItem);
}
