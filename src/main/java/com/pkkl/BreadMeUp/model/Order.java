package com.pkkl.BreadMeUp.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
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
    private int id;

    @Column(name = "order_receive")
    private Date date;

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "bakery_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bakery bakery;

    @OneToMany
    @JoinColumn(name = "order_product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderProduct> orderProducts;

    @Version
    @Column(name = "version")
    private long version;
}
