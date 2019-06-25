package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.model.OrderProduct;

public class OrderTotalCostStrategy implements DiscountStrategy {
    private static final double CHEAP_ORDER_THRESHOLD = 20.0;
    private static final double NORMAL_ORDER_THRESHOLD = 50.0;
    private static final double EXPENSIVE_ORDER_THRESHOLD = 100.0;

    private static final double CHEAP_DISCOUNT_PERCENTAGE = 0.5;
    private static final double NORMAL_DISCOUNT_PERCENTAGE = 0.8;
    private static final double EXPENSIVE_DISCOUNT_PERCENTAGE = 0.10;

    @Override
    public double calculateDiscountPercentage(Order order) {
        double totalOrderCost = order.getOrderProducts().stream().mapToDouble(OrderProduct::getPrice).sum();
        double discountPercentage = 0.0;

        if (totalOrderCost >= EXPENSIVE_ORDER_THRESHOLD) {
            discountPercentage = EXPENSIVE_DISCOUNT_PERCENTAGE;
        }
        else if (totalOrderCost >= NORMAL_ORDER_THRESHOLD) {
            discountPercentage = NORMAL_DISCOUNT_PERCENTAGE;
        }
        else if (totalOrderCost >= CHEAP_ORDER_THRESHOLD) {
            discountPercentage = CHEAP_DISCOUNT_PERCENTAGE;
        }

        return discountPercentage;
    }

}
