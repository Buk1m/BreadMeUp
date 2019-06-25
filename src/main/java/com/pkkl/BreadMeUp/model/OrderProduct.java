package com.pkkl.BreadMeUp.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "order_products")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    @EqualsAndHashCode.Exclude
    private int id;

    @NotBlank(message = "Order product name cannot be blank")
    @Length(min = 3, max = 64, message = "Order product name length should be from 3 to 64")
    @Column(name = "product_name")
    private String productName;

    @Column(name = "price", precision = 5, scale = 2)
    @Min(0)
    private double price;

    @Column(name = "amount")
    @Min(1)
    private int amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    @Version
    @Column(name = "version")
    private long version;
}
