package com.example.myShop.service.impl;

import com.example.myShop.domain.dto.selectedProduct.SelectedProductDto;
import com.example.myShop.domain.entity.*;
import com.example.myShop.domain.exception.SelectedProductCheckCountException;
import com.example.myShop.domain.exception.SelectedProductChangeException;
import com.example.myShop.domain.exception.SelectedProductNotFoundException;
import com.example.myShop.domain.mapper.SelectedProductMapper;
import com.example.myShop.repository.SelectedProductRepository;
import com.example.myShop.service.GoodsService;
import com.example.myShop.service.OrderService;
import com.example.myShop.service.SelectedProductService;
import com.example.myShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author nafis
 * @since 10.02.2022
 */

@Service
@Transactional
@RequiredArgsConstructor
public class SelectedProductServiceImpl implements SelectedProductService {
    private final SelectedProductRepository selectedProductRepository;
    private final SelectedProductMapper selectedProductMapper;
    private final GoodsService goodsService;
    private final OrderService orderService;
    private final UserService userService;

    @Override
    public SelectedProduct getAndInitialize(Integer id) {
        SelectedProduct result = selectedProductRepository
                .findById(id)
                .orElseThrow(() -> new SelectedProductNotFoundException(id));
        Hibernate.initialize(result);
        Hibernate.initialize(result.getOrder());
        Hibernate.initialize(result.getOrder().getUser());
        Hibernate.initialize(result.getOrder().getReceiving());
        Hibernate.initialize(result.getGoods());
        Hibernate.initialize(result.getGoods().getProducer());
        Hibernate.initialize(result.getGoods().getCategory());
        return result;
    }

    @Override
    public Map<String, Object> getAndInitializeAll(Integer orderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SelectedProduct> selectedProductPage = selectedProductRepository.findAllByOrderId(pageable, orderId);
        List<SelectedProductDto> listTemp = new ArrayList<>();

        for(SelectedProduct selectedProduct : selectedProductPage){
            Hibernate.initialize(selectedProduct);
            Hibernate.initialize(selectedProduct.getOrder());
            Hibernate.initialize(selectedProduct.getOrder().getUser());
            Hibernate.initialize(selectedProduct.getOrder().getReceiving());
            Hibernate.initialize(selectedProduct.getGoods());
            Hibernate.initialize(selectedProduct.getGoods().getProducer());
            Hibernate.initialize(selectedProduct.getGoods().getCategory());

            listTemp.add(selectedProductMapper.toDto(selectedProduct));
        }

        Page<SelectedProductDto> result = new PageImpl<>(listTemp);
        Map<String, Object> response = new HashMap<>();

        response.put("selectedProducts", result.getContent());
        response.put("currentPage", result.getNumber());
        response.put("totalItems", result.getTotalElements());
        response.put("totalPages", result.getTotalPages());

        return response;
    }

    private boolean checkCount(long count, Integer goodsId){
        Goods goods = goodsService.getAndInitialize(goodsId);
        long availability = goods.getCount();

        return count <= availability;
    }

    private BigDecimal computePrice(Goods goods, long count){
        //Reduce count of goods
        goods.decCount(count);
        goodsService.update(goods.getId(), goods);

        BigDecimal countBigDec = new BigDecimal(count);

        return goods.getPrice().multiply(countBigDec);
    }

    private Order getOrder(BigDecimal price, Integer userId){
        User user = userService.getAndInitialize(userId);
        List<Order> createdOrders = user.getOrders();

        for(Order order : createdOrders){
            if(Objects.equals(order.getOrderStatus().getStatus(), "Creating")){
                // Increase the price
                order.addPrice(price);
                orderService.update(order.getId(), order);

                return order;
            }
        }

        return orderService.create(price, userId);
    }

    @Override
    public SelectedProduct create(SelectedProduct selectedProduct, Integer goodsId, Integer userId) {
        Goods goods = goodsService.getAndInitialize(goodsId);
        long count = selectedProduct.getCount();

        if(!checkCount(count, goodsId)){
            throw new SelectedProductCheckCountException(goodsId);
        }

        selectedProduct.setPrice(computePrice(goods, count));
        selectedProduct.setOrder(getOrder(selectedProduct.getPrice(), userId));
        selectedProduct.setGoods(goods);

        return selectedProductRepository.save(selectedProduct);
    }

    @Override
    public SelectedProduct update(Integer id, SelectedProduct selectedProduct) {
        SelectedProduct selProductFromDB = getAndInitialize(id);

        OrderStatus status = selProductFromDB.getOrder().getOrderStatus();
        checkOrderStatus(status);

        BigDecimal newPrice = computePrice(selectedProduct.getGoods(), selectedProduct.getCount());
        selectedProduct.setPrice(newPrice);

        // Refresh price of order
        BigDecimal differenceOfPrice = newPrice.subtract(selProductFromDB.getPrice());
        Order order = selectedProduct.getOrder();
        order.addPrice(differenceOfPrice);
        orderService.update(order.getId(), order);

        return Optional.of(selProductFromDB)
                .map(current -> selectedProductMapper.merge(current, selectedProduct))
                .map(selectedProductRepository::save)
                .orElseThrow();
    }

    @Override
    public void delete(Integer id) {
        SelectedProduct selectedProduct = getAndInitialize(id);
        Order order = orderService.getAndInitialize(selectedProduct
                                                    .getOrder()
                                                    .getId() );
        checkOrderStatus(order.getOrderStatus());

        // Order cannot exist without selected product
        if (order.getSelectedProducts().size() == 1){
            orderService.delete(order.getId());
        } else {
            selectedProductRepository.deleteById(id);
        }
    }

    // SelectelProduct may be changed if only order status is 'Creating'
    private void checkOrderStatus(OrderStatus status){
        if(!status.equals(OrderStatus.CREATING)){
            throw new SelectedProductChangeException("SelectedProduct is containing in order with status " +
                    status.getStatus() +
                    ". It must be: creating");
        }
    }
}
