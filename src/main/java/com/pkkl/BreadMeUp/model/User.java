package com.pkkl.BreadMeUp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;

    @Column(name = "login", unique = true)
    @NotBlank(message = "Login cannot be empty")
    private String login;

    @Column(name = "password", unique = true)
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Column(name = "email", unique = true)
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Column(name = "phone", unique = true)
    @Pattern(regexp = "\\d{9}", message = "Phone is not valid")
    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
