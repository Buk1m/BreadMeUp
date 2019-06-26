package com.pkkl.BreadMeUp.services;import com.pkkl.BreadMeUp.dtos.DiscountDto;

import com.pkkl.BreadMeUp.dtos.OrderPriceDto;
import com.pkkl.BreadMeUp.model.DiscountType;
import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.model.OrderProduct;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface OrderService {
    Order getById(int id);

    List<Order> getAll();

    Order update(Order order);

    void cancelOrder(int id);

    Order add(Order order, List<OrderProduct> orderProducts, Principal principal);

    List<Order> getByBakeryId(int id);

    List<Order> getByUserId(int id);

    OrderPriceDto completeOrder(int orderId, Principal principal, Set<DiscountType> discountDto);

    List<Order> getOrdersByPrincipal(Principal principal);
}
