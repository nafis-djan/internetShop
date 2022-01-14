package com.example.myShop.service;

import com.example.myShop.domain.entity.Order;
import org.springframework.data.domain.Page;

/**
 * @author nafis
 * @since 19.12.2021
 */

public interface OrderService {
    Order get(Integer id);

    Order create(Order order, Integer goodsId, Integer receiveId, Integer payId, Integer userId);

    Order update(Integer id, Order order);

    void delete(Integer id);

    Page<Order> getOrders();
}
