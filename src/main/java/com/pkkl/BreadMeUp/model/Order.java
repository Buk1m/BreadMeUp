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
    @EqualsAndHashCode.Exclude
    private int id;

    @Column(name = "order_receive")
    private Date date;

    @Column(name = "completed")
    private boolean completed;

    @Column(name = "cancelled")
    private boolean cancelled;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne()
    @JoinColumn(name = "bakery_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bakery bakery;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_product_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderProduct> orderProducts;

    @Version
    @Column(name = "version")
    private long version;
}
