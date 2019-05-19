package com.pkkl.BreadMeUp.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Data

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;

    @Column(name = "name")
    @Length(min = 3, max = 25, message = "Name length should be from 3 to 25")
    private String name;

    @Version
    @Column(name = "version")
    private long version;
}
