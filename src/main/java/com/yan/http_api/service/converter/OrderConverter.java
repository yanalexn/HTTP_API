package com.yan.http_api.service.converter;

import com.yan.http_api.entity.Orderr;
import com.yan.http_api.service.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderConverter {

    public OrderDto entityToDto(Orderr entity) {
        return new OrderDto()
                .setId(entity.getId())
                .setCreationDate(entity.getCreationDate())
                .setDescriptionn(entity.getDescriptionn())
                .setCost(entity.getCost());
    }

    public List<OrderDto> entityToDto(List<Orderr> entities) {
        return entities.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
