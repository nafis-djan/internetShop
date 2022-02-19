package com.example.myShop.service;

import com.example.myShop.domain.entity.Goods;
import com.example.myShop.domain.entity.Order;
import com.example.myShop.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * @author nafis
 * @since 19.12.2021
 */

public interface OrderService {
    Order get(Integer id);

    Order getAndInitialize(Integer id);

    Page<Order> getAllByUserIdAndInit(Integer userId, Pageable pageable);

    Order getByUserAndOrderStatus(Integer userId, OrderStatus orderStatus);

    Page<Order> getAllByReceivingIdAndInit(Integer receivingId, Pageable pageable);

    Order create(BigDecimal price, Integer userId);

    void checkCount(Long count, Goods goods);

    Order update(Integer id, Order order, Integer receivingId);

    void refreshTotalPrice(BigDecimal oldPrice, BigDecimal newPrice, Order order);

    void delete(Integer id);
}
