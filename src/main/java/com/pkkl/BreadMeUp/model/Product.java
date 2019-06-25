package com.pkkl.BreadMeUp.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


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

    @NotBlank(message = "Name cannot be blank")
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    @Column(name = "name")
    private String name;

    @Column(name = "price", precision = 5, scale = 2)
    @Min(0)
    private double price;

    @Column(name = "product_limit")
    @Min(0)
    private int limit;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(optional = false)
    private Bakery bakery;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne(optional = false)
    private ProductType productType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductAvailability> productAvailability;

    @Version
    @Column(name = "version")
    private long version;
}
