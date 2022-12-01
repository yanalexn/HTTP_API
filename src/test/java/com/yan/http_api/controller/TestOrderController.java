package com.yan.http_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.entity.Orderr;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.service.converter.AddressConverter;
import com.yan.http_api.service.converter.ClientConverter;
import com.yan.http_api.service.converter.OrderConverter;
import com.yan.http_api.service.dto.ClientDto;
import com.yan.http_api.service.dto.OrderDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class TestOrderController {

    //    @Autowired
    @MockBean
    private MockMvc mockMvc;

    //    @Autowired
    @MockBean
    private ClientRepo mockClientRepo;

    //    @Autowired
    @MockBean
    private OrderConverter orderConverter;


    private List<Orderr> ordersObj;
    private List<OrderDto> orderDtosObj;

    private List<String> orders;
    private List<String> orderDtos;

    private String objToJsonString(Object o) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(o);
    }


    @Test
    @DisplayName("[Post] Добавление 3-ёх заказов к клиенту с id=1")
    public void test1() throws Exception {
        mockClientRepo.save(new Client("gerasim", "11", "+11",
                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
                null));

        ordersObj = new ArrayList<>();
        orderDtosObj = new ArrayList<>();

        ordersObj.add(new Orderr(LocalDate.of(2022, 11, 15), "посылка", 156));
        ordersObj.add(new Orderr(LocalDate.of(2022, 11, 17), "бандероль", 999));
        ordersObj.add(new Orderr(LocalDate.of(2022, 11, 19), "письмо", 55));

        orderDtosObj = orderConverter.entityToDto(ordersObj);
        for (int i = 0; i < 3; i++) {
            orderDtosObj.get(i).setId((long) (i + 1));
        }

        orders = ordersObj.stream()
                .map(order -> {
                    try {
                        return objToJsonString(order);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException();
                    }
                }).collect(Collectors.toList());
        orderDtos = orderDtosObj.stream()
                .map(dto -> {
                    try {
                        return objToJsonString(dto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException();
                    }
                }).collect(Collectors.toList());


        mockMvc.perform(post("/order/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(ordersObj)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(orderDtosObj)));
    }

//    @Test
//    void get() {
//    }
//
//    @Test
//    void put() {
//    }
//
//    @Test
//    void getAllForClient() {
//    }
//
//    @Test
//    void delete() {
//    }
}