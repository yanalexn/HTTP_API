package com.yan.http_api.service.converter;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.service.dto.AddressDto;
import com.yan.http_api.service.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientConverter {

    private final AddressConverter addressConverter;

    public ClientDto entityToDto(Client entity) {
        return new ClientDto()
                .setId(entity.getId())
                .setName(entity.getName())
                .setInn(entity.getInn())
                .setPhone(entity.getPhone())
                .setAddress(addressConverter.entityToDto(entity.getAddress()));
    }

    public List<ClientDto> entityToDto(List<Client> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
