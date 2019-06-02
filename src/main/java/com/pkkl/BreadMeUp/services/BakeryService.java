package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.model.Order;

import java.util.List;

public interface BakeryService {
    Bakery getById(int id);
    List<Bakery> getAll();
    void delete(int id);
    Bakery update(Bakery bakery);
    Bakery add(Bakery bakery);
}
