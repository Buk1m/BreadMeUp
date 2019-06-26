package com.pkkl.BreadMeUp.discounts;

import com.pkkl.BreadMeUp.model.Order;
import java.time.LocalDateTime;

public class HappyHoursStrategy implements DiscountStrategy {

    private static final int EARLY_BIRD_HOUR_THRESHOLD = 8;
    private static final int BETTER_LATE_THEN_NEVER_THRESHOLD = 18;
    private static final double DISCOUNT_PERCENTAGE = 0.2;

    @Override
    public double calculateDiscountPercentage(Order order) {
        int currentHour = LocalDateTime.now().getHour();
        if (currentHour < EARLY_BIRD_HOUR_THRESHOLD || currentHour > BETTER_LATE_THEN_NEVER_THRESHOLD)
            return DISCOUNT_PERCENTAGE;

            return 0.0;
    }
}
