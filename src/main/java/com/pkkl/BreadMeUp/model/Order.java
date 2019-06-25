package com.pkkl.BreadMeUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkkl.BreadMeUp.discounts.DiscountStrategy;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @EqualsAndHashCode.Exclude
    private int id;

    @Column(name = "order_receive", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "cancelled")
    private boolean cancelled;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "bakery_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bakery bakery;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonIgnore
    @ToString.Exclude
    private Set<OrderProduct> orderProducts;

    @Version
    @Column(name = "version")
    private long version;

    public double calculateTotalPrice(Set<DiscountStrategy> discountStrategy) {
        double totalPrice = calculatePrice();
        double discount = discountStrategy.stream().mapToDouble(ds -> ds.calculateDiscountPercentage(this)).sum();
        return Math.round((1 - discount) * totalPrice * 100) / 100.0;
    }

    private double calculatePrice() {
        return orderProducts.stream().mapToDouble(op -> op.getAmount() * op.getPrice()).sum();
    }
}
