package com.pkkl.BreadMeUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Product product;

    @Column(name = "day", nullable = false)
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

    public void appendToOrderedNumber(int orderedNumber) {
        int appended = this.orderedNumber + orderedNumber;
        if (appended > limit) {
            throw new ConstraintException("Order exceeded the limit by " + (appended - limit));
        }
        this.orderedNumber = appended;
    }
}
