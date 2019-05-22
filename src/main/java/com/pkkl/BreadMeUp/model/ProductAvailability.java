package com.pkkl.BreadMeUp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "products_availability")
public class ProductAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private int id;

    @Column(name = "day")
    private LocalDate date;

    @Column(name = "ordered_number")
    @Min(0)
    private int orderedNumber;

    @Column(name = "product_limit")
    @Min(0)
    private int limit;

    @Version
    @Column(name = "version")
    private long version;
}
