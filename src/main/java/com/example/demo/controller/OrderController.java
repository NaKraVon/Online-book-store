package com.example.demo.controller;

import com.example.demo.dto.order.OrderRequestDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.dto.orderitem.OrderItemsResponseDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Controller", description = "Endpoints for managing the Orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create a new order for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                        @AuthenticationPrincipal User user) {
        return orderService.createOrder(orderRequestDto, user);
    }

    @Operation(summary = "Get all orders of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(array =
                    @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Set<OrderResponseDto> getAllUserOrders(@AuthenticationPrincipal User user) {
        return orderService.getByAllOrdersByUserId(user.getId());
    }

    @Operation(summary = "Update the status of an order (ADMIN only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden – only ADMIN can access"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(@RequestBody @Valid
                                              UpdateOrderStatusRequestDto
                                                      updateOrderStatusRequestDto,
                                              @PathVariable Long id,
                                              @AuthenticationPrincipal User user) {
        return orderService.updateOrderStatus(updateOrderStatusRequestDto, id, user);
    }

    @GetMapping("/{id}/items")
    public Set<OrderItemsResponseDto> getAllOrderItemsInOrder(@PathVariable Long id,
                                                              @AuthenticationPrincipal User user) {
        return orderService.getAllOrderItemsInOrder(id, user.getId());
    }

    @GetMapping("/{id}/items/{itemId}")
    public OrderItemsResponseDto getOrderItemsInOrder(@PathVariable Long id,
                                                      @PathVariable Long itemId,
                                                      @AuthenticationPrincipal User user) {
        return orderService.getOrderItemInOrder(id, itemId, user.getId());
    }
}
