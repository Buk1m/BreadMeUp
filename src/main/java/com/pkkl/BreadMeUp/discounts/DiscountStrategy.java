package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;

public interface DiscountStrategy {
    double calculateDiscountPercentage(Order order);
}
