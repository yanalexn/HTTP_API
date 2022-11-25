package com.yan.http_api.service.dto;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * A DTO for the {@link Client} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClientDto implements Serializable {
    private Long id;
    private String name;
    private String inn;
    private String phone;
    private Address address;
}