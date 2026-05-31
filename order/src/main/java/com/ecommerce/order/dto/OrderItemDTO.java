package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItemDTO {

    private Long Id;
    private BigDecimal price;
    private Long productId;
    private Integer quantity;
}
