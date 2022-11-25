package com.yan.http_api.controller;

import com.yan.http_api.entity.Orderr;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ClientRepo clientRepo;
    private final OrderRepo orderRepo;

    @PostMapping("/post")
    public void post(@RequestBody Orderr orderr) {
        orderRepo.save(orderr);
    }

    @GetMapping("/get")
    public List<Orderr> get() {
        return orderRepo.findAll();
    }
}
