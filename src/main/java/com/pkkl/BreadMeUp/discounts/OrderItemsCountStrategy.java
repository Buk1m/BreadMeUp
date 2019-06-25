package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;
import com.pkkl.BreadMeUp.model.OrderProduct;

public class OrderItemsCountStrategy implements DiscountStrategy {

    private static final int CASUAL_THRESHOLD = 5;
    private static final int BIG_FAMILY_THRESHOLD = 10;
    private static final int PASTRY_LOVER_THRESHOLD = 15;

    private static final double CASUAL_DISCOUNT_PERCENTAGE = 0.5;
    private static final double BIG_FAMILY_DISCOUNT_PERCENTAGE = 0.8;
    private static final double PASTRY_LOVER_DISCOUNT_PERCENTAGE = 0.10;


    @Override
    public double calculateDiscountPercentage(Order order) {
        int productAmount = order.getOrderProducts().stream().mapToInt(OrderProduct::getAmount).sum();
        double discountPercentage = 0.0;

        if (productAmount >= PASTRY_LOVER_THRESHOLD) {
            discountPercentage = PASTRY_LOVER_DISCOUNT_PERCENTAGE;
        } else if (productAmount >= BIG_FAMILY_THRESHOLD) {
            discountPercentage = BIG_FAMILY_DISCOUNT_PERCENTAGE;
        } else if (productAmount >= CASUAL_THRESHOLD) {
            discountPercentage = CASUAL_DISCOUNT_PERCENTAGE;
        }

        return discountPercentage;
    }
}
