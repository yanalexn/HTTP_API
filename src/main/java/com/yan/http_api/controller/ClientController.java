package com.yan.http_api.controller;

import com.yan.http_api.entity.Client;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientRepo clientRepo;
    private final OrderRepo orderRepo;

    @PostMapping("/post")
    public void post(@RequestBody Client client) {
        clientRepo.save(client);
    }
}
