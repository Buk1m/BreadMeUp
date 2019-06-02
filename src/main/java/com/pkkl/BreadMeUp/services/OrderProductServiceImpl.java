package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.OrderProduct;
import com.pkkl.BreadMeUp.repositories.OrderProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderProductServiceImpl extends BaseService<OrderProduct, OrderProductRepository> implements OrderProductService {

    @Override
    public OrderProduct getById(int id) {
        try {
            return repository.findById(id).orElseThrow(() -> new RuntimeException("Order product doesn't exist \\U+1F635"));
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<OrderProduct> getAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public OrderProduct update(OrderProduct orderProduct) {
        //TODO: add aggregation logic
        return saveOrUpdate(orderProduct);

    }

    @Override
    //TODO: add aggregation logic
    public OrderProduct add(OrderProduct orderProduct) {
        return saveOrUpdate(orderProduct);
    }

    @Override
    public List<OrderProduct> getByOrderId(int id) {
        try {
            return this.repository.findAll().stream().filter(o -> o.getId() == id).collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
