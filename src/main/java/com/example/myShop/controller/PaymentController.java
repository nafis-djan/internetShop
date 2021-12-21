package com.example.myShop.controller;

import com.example.myShop.domain.dto.PaymentDto;
import com.example.myShop.domain.dto.PaymentNotIdDto;
import com.example.myShop.domain.mapper.PaymentMapper;
import com.example.myShop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author nafis
 * @since 20.12.2021
 */

@Controller
@RequestMapping(path = "payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping("{id}")
    public PaymentDto get(@PathVariable("id") Integer id){
        return Optional.of(id)
                .map(paymentService::get)
                .map(paymentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Payment method not found: " + id));
    }

    @PostMapping()
    public PaymentDto create(@RequestBody PaymentNotIdDto paymentDto){
        return Optional.ofNullable(paymentDto)
                .map(paymentMapper::fromNotIdDto)
                .map(paymentService::create)
                .map(paymentMapper::toDto)
                .orElseThrow();
    }

    @PatchMapping("{id}")
    public PaymentDto update(@PathVariable("id") Integer id, PaymentNotIdDto paymentDto){
        return Optional.ofNullable(paymentDto)
                .map(paymentMapper::fromNotIdDto)
                .map(toUpdate -> paymentService.update(toUpdate, id))
                .map(paymentMapper::toDto)
                .orElseThrow();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Integer id){
        paymentService.delete(id);
    }
}
