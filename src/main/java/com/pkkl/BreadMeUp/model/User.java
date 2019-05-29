package com.pkkl.BreadMeUp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
public class User implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "login", unique = true)
    @Length(min = 3, max = 64, message = "Login length should be from 3 to 64")
    @NotBlank(message = "Login cannot be blank")
    private String login;

    @Column(name = "password", unique = true)
    @Length(min = 8, max = 64, message = "Password length should be from 8 to 64")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(name = "email", unique = true)
    @Email(message = "Email is not valid")
    @Length(max = 64, message = "Email length should be to 64")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Column(name = "phone", unique = true)
    @Pattern(regexp = "\\d{7,15}", message = "Phone is not valid")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    private boolean blocked;

    @Version
    @Column(name = "version")
    private long version;

    @Transient
    private boolean update;

    @Override
    public boolean isNew() {
        return !this.update;
    }
}
