package com.pkkl.BreadMeUp.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Data

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "name", unique = true)
    @Length(min = 3, max = 20, message = "Name length should be from 3 to 20")
    private String name;
}
