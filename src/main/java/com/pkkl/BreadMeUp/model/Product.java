package com.pkkl.BreadMeUp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @NotBlank
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    @Column(name = "name")
    private String name;

    @Column(name = "price", precision = 5, scale = 2)
    private double price;

    @Column(name = "product_limit")
    private int limit;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(cascade = CascadeType.ALL)
    private Bakery bakery;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    private ProductType productType;

    @ManyToMany
    @JoinTable(name = "product_availability", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "product_availability_id"))
    private Set<ProductAvailability> productAvailability;

    @Version
    @Column(name = "version")
    private long version;
}
