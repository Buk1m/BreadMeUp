package com.pkkl.BreadMeUp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "product_types")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    private int id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "unit_of_measurement")
    private UnitOfMeasurement unitOfMeasurement;

    @Column(name = "size", precision = 6, scale = 3)
    @Min(0)
    private double size;

    @Version
    @Column(name = "version")
    private long version;
}
