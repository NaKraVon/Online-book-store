package com.example.demo.repository.order;

import com.example.demo.model.Order;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Set<Order>> findAllOrdersByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.id = :orderId")
    Optional<Order> findOrderWithIdByUserId(@Param("userId") Long userId,
                                            @Param("orderId") Long orderId);
}
