package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;

public class NormalStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscountPercentage(Order order) {
        return 0.1;
    }
}
