package com.pkkl.BreadMeUp.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data

@Entity
@Table(name = "bakeries")
public class Bakery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bakery_id")
    private int id;

    @Column(name = "place_id", unique = true)
    private String placeId;

    @Column(name = "name", unique = true)
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    private String name;

    @Column(name = "city")
    @Length(min = 3, max = 64, message = "City length should be from 3 to 64")
    private String city;

    @Column(name = "postal_code")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code is not valid")
    private String postalCode;

    @Column(name = "street_name")
    @Length(min = 3, max = 64, message = "Street name length should be from 3 to 64")
    private String streetName;

    @Column(name = "street_number")
    @Length(min = 1, max = 15, message = "Street number length should be from 1 to 15")
    private String streetNumber;

    @Version
    @Column(name = "version")
    private long version;
}