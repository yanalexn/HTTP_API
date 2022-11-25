package com.yan.http_api.controller;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Orderr;
import com.yan.http_api.exception_handling.ExceptionMessage;
import com.yan.http_api.exception_handling.HttpApiException;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.repo.OrderRepo;
import com.yan.http_api.service.converter.OrderConverter;
import com.yan.http_api.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ClientRepo clientRepo;
    private final OrderRepo orderRepo;
    private final OrderConverter orderConverter;

    @PostMapping("/post/{clientId}")
    public List<OrderDto> post(@PathVariable Long clientId, @Valid @RequestBody List<Orderr> orders) {
        orders.forEach(order -> order.setClient(clientRepo.findById(clientId)
                .orElseThrow(() -> new HttpApiException(ExceptionMessage.CLIENT_ID.getMessage()))));
        return orderConverter.entityToDto(orderRepo.saveAll(orders));
    }

    @GetMapping("/get_by_id/{id}")
    public OrderDto get(@PathVariable Long id) {
        return orderConverter.entityToDto(orderRepo.findById(id)
                .orElseThrow(() -> new HttpApiException(ExceptionMessage.ORDER_ID.getMessage())));
    }

    @PutMapping("/put/{id}")
    public OrderDto put(@PathVariable Long id, @RequestBody Orderr order) {
        return orderConverter.entityToDto(
                orderRepo.findById(id)
                        .map(old -> {
                            old.setCreationDate(order.getCreationDate())
                                    .setDescriptionn(order.getDescriptionn())
                                    .setCost(order.getCost());
                            return orderRepo.save(old);
                        })
                        .orElseThrow(() -> new HttpApiException(ExceptionMessage.ORDER_ID.getMessage())));
    }

    @GetMapping("/get_all_for_client/{clientId}")
    public List<OrderDto> getAllForClient(@PathVariable Long clientId) {
        if (!clientRepo.existsById(clientId))
            throw new HttpApiException(ExceptionMessage.CLIENT_ID.getMessage());
        return orderConverter.entityToDto(orderRepo.findAllByClient_Id(clientId));
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        if (!orderRepo.existsById(id))
            throw new HttpApiException(ExceptionMessage.ORDER_ID.getMessage());
        orderRepo.deleteById(id);
        return "The order is successfully deleted";
    }
}
