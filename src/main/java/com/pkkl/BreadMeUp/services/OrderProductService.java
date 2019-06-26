package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.OrderProduct;

import java.util.List;

public interface OrderProductService {
    OrderProduct getById(int id);

    List<OrderProduct> getAll();

    OrderProduct update(OrderProduct orderProduct);

    OrderProduct add(OrderProduct orderProduct);

    List<OrderProduct> getByOrderId(int id);
}
