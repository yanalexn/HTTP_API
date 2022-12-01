package com.yan.http_api.controller;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.exception_handling.ExceptionMessage;
import com.yan.http_api.exception_handling.HttpApiException;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.service.converter.ClientConverter;
import com.yan.http_api.service.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientRepo clientRepo;
    private final ClientConverter clientConverter;

    @PostMapping("/post")
    public ClientDto post(@Valid @RequestBody Client client) {
        return clientConverter.entityToDto(clientRepo.save(client));
    }

    @GetMapping("/get_by_id/{id}")
    public ClientDto get(@PathVariable Long id) {
        return clientConverter.entityToDto(clientRepo.findById(id)
                .orElseThrow(() -> new HttpApiException(ExceptionMessage.CLIENT_ID.getMessage())));
    }

    @GetMapping("/get_by_name/{name}")
    public ClientDto get(@PathVariable String name) {
        return clientConverter.entityToDto(clientRepo.findByName(name)
                .orElseThrow(() -> new HttpApiException(ExceptionMessage.NAME.getMessage())));
    }

    @GetMapping("/get_all")
    public List<ClientDto> get() {
        return clientConverter.entityToDto(clientRepo.findAll());
    }

    @PutMapping("/put/{id}")
    public ClientDto put(@PathVariable Long id, @Valid @RequestBody Client client) {
        Address address = client.getAddress();
        return clientConverter.entityToDto(
                clientRepo.findById(id)
                        .map(old -> {
                            if (address != null) {
                                if (old.getAddress() == null) {
                                    old.setAddress(new Address());
                                }
                                old.getAddress()
                                        .setRegionCode(address.getRegionCode())
                                        .setRegionName(address.getRegionName())
                                        .setCity(address.getCity())
                                        .setStreet(address.getStreet())
                                        .setHouse(address.getHouse())
                                        .setBuilding(address.getBuilding())
                                        .setOffice(address.getOffice());
                            }
                            old.setName(client.getName())
                                    .setInn(client.getInn())
                                    .setPhone(client.getPhone());
                            return clientRepo.save(old);
                        })
                        .orElseThrow(() -> new HttpApiException(ExceptionMessage.CLIENT_ID.getMessage())));
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        if (!clientRepo.existsById(id))
            throw new HttpApiException(ExceptionMessage.CLIENT_ID.getMessage());
        clientRepo.deleteById(id);
        return "Клиент успешно удалён";
    }
}
