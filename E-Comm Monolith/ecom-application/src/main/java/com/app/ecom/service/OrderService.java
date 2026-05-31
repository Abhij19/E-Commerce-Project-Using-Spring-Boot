package com.app.ecom.service;

import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()) {
            return Optional.empty();
        }
        // Validate for user
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        // Since we do not have order items which we need to create order we transform cart item to order item
        List<OrderItem> orderItemList = cartItems.stream().map(cartItem ->
            new OrderItem(
                    null,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getPrice(),
                    order
            )).toList();
        order.setItems(orderItemList);
        Order savedOrder = orderRepository.save(order);
        // Clear the cart
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems().stream().map(orderItem -> new OrderItemDTO (
                        orderItem.getId(),
                        orderItem.getPrice(),
                        orderItem.getProduct().getId(),
                        orderItem.getQuantity()
                )).toList(),
                savedOrder.getCreatedAt()
        );
    }
}
