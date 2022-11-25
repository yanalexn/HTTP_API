package com.yan.http_api.service.converter;

import com.yan.http_api.entity.Client;
import com.yan.http_api.service.dto.ClientDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientConverter {

    public ClientDto entityToDto(Client entity) {
        return new ClientDto()
                .setId(entity.getId())
                .setName(entity.getName())
                .setInn(entity.getInn())
                .setPhone(entity.getPhone())
                .setAddress(entity.getAddress());
    }

    public List<ClientDto> entityToDto(List<Client> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
