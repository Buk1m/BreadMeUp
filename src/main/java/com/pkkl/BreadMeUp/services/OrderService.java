package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Order;

import java.util.List;

public interface OrderService {
    Order getById(int id);

    List<Order> getAll();

    Order update(Order order);

    void setCanceled(int id, boolean canceled);

    Order add(Order order);

    List<Order> getByBakeryId(int id);

    List<Order> getByUserId(int id);

}
