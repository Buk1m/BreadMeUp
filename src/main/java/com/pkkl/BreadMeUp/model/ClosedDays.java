package com.pkkl.BreadMeUp.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data

@Entity
@Table(name = "closed_days")
public class ClosedDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closed_day_id")
    private int id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "bakeries_closed_days", joinColumns = @JoinColumn(name = "closed_day_id"), inverseJoinColumns = @JoinColumn(name = "bakery_id"))
    private Set<Bakery> bakeries;

    @Version
    @Column(name = "version")
    private long version;
}
