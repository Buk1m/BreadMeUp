package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j

@Service
public class OrderServiceImpl extends BaseService<Order, OrderRepository> implements OrderService {

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.repository = orderRepository;
    }

    @Override
    public Order getById(int id) {
        try {
            return repository.findById(id).orElseThrow(() -> new RuntimeException("Order doesn't exist \\U+1F635"));
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public void setCanceled(int id, boolean isCancelled) {
        try {
            repository.setCancelled(id, isCancelled);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Order update(final Order order) {
        return saveOrUpdate(order);
    }

    @Override
    public Order add(final Order order) {
        return saveOrUpdate(order);
    }

    @Override
    public List<Order> getByBakeryId(int id) {

        try {
            return repository.findAll().stream()
                    .filter(o -> o.getBakery().getId() == id)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> getByUserId(int id) {

        try {
            return repository.findAll().stream()
                    .filter(o -> Objects.equals(o.getUser().getId(), id))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
