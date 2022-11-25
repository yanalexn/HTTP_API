package com.yan.http_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Integer regionCode;
    private String regionName;
    private String city;
    private String street;
    private Integer house;
    private Integer building;
    private Integer office;
}
