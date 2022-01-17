package com.example.myShop.service;

import com.example.myShop.domain.entity.Payment;

import java.util.List;

/**
 * @author nafis
 * @since 19.12.2021
 */
public interface PaymentService {
    Payment get(Integer id);

    List<Payment> getAll();

    Payment create(Payment payment);

    Payment update(Payment payment, Integer id);

    void delete(Integer id);
}
