package com.pkkl.BreadMeUp.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "bakeries")
public class Bakery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bakery_id")
    private int id;

    @Column(name = "place_id", unique = true)
    @NotBlank(message = "PlaceId cannot be blank")
    private String placeId;

    @Column(name = "name", unique = true)
    @NotBlank(message = "Name cannot be blank")
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    private String name;

    @Column(name = "city")
    @NotBlank(message = "City cannot be blank")
    @Length(min = 3, max = 64, message = "City length should be from 3 to 64")
    private String city;

    @Column(name = "postal_code")
    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code is not valid")
    private String postalCode;

    @Column(name = "street_name")
    @NotBlank(message = "Street name cannot be blank")
    @Length(min = 3, max = 64, message = "Street name length should be from 3 to 64")
    private String streetName;

    @Column(name = "street_number")
    @NotBlank(message = "Street number cannot be blank")
    @Length(min = 1, max = 15, message = "Street number length should be from 1 to 15")
    private String streetNumber;

    @OneToMany(mappedBy = "bakery")
    @ToString.Exclude
    private Set<Order> orders;

    @Version
    @Column(name = "version")
    private long version;
}