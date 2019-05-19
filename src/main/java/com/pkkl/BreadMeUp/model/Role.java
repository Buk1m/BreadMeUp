package com.pkkl.BreadMeUp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name", unique = true)
    private String name;
}
