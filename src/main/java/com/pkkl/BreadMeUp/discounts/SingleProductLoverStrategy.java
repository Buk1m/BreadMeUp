package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.model.OrderProduct;
import com.pkkl.BreadMeUp.model.Product;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class SingleProductLoverStrategy implements DiscountStrategy {

    private static final int CASUAL_THRESHOLD = 5;
    private static final int BIG_FAMILY_THRESHOLD = 10;
    private static final int PASTRY_LOVER_THRESHOLD = 15;

    private static final double CASUAL_DISCOUNT_PERCENTAGE = 0.01;
    private static final double BIG_FAMILY_DISCOUNT_PERCENTAGE = 0.02;
    private static final double PASTRY_LOVER_DISCOUNT_PERCENTAGE = 0.03;

    private static final double MAXIMAL_DISCOUNT = 0.2;

    @Override
    public double calculateDiscountPercentage(Order order) {

        double discountPercentage = 0.0;

        Map<Product, Integer> test = order.getOrderProducts().stream().collect(groupingBy(OrderProduct::getProduct, Collectors.summingInt(OrderProduct::getAmount)));
        for (var productAmount : test.values()) {
            if (productAmount >= PASTRY_LOVER_THRESHOLD) {
                discountPercentage += PASTRY_LOVER_DISCOUNT_PERCENTAGE;
            } else if (productAmount >= BIG_FAMILY_THRESHOLD) {
                discountPercentage += BIG_FAMILY_DISCOUNT_PERCENTAGE;
            } else if (productAmount >= CASUAL_THRESHOLD) {
                discountPercentage += CASUAL_DISCOUNT_PERCENTAGE;
            }
            if (discountPercentage > MAXIMAL_DISCOUNT)
                return MAXIMAL_DISCOUNT;
        }

        return discountPercentage;
    }
}
