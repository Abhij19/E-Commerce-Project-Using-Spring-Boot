package com.app.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemDTO {

    private Long Id;
    private BigDecimal price;
    private Long productId;
    private Integer quantity;
}
