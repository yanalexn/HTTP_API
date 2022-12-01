package com.yan.http_api.service.converter;

import com.yan.http_api.entity.Address;
import com.yan.http_api.service.dto.AddressDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressConverter {

    public AddressDto entityToDto(Address entity) {
        return new AddressDto()
                .setId(entity.getId())
                .setRegionCode(entity.getRegionCode())
                .setRegionName(entity.getRegionName())
                .setCity(entity.getCity())
                .setStreet(entity.getStreet())
                .setHouse(entity.getHouse())
                .setBuilding(entity.getBuilding())
                .setOffice(entity.getOffice());
    }

    public List<AddressDto> entityToDto(List<Address> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
