package com.yan.http_api.service.dto;

import com.yan.http_api.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * A DTO for the {@link Address} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddressDto implements Serializable {
    private Long id;
    private Integer regionCode;
    private String regionName;
    private String city;
    private String street;
    private Integer house;
    private Integer building;
    private Integer office;
}