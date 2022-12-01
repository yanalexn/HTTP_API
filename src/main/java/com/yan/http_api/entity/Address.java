package com.yan.http_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer regionCode;
    private String regionName;
    private String city;
    private String street;
    private Integer house;
    private Integer building;
    private Integer office;

    public Address(Integer regionCode, String regionName, String city, String street, Integer house, Integer building, Integer office) {
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.office = office;
    }
}
