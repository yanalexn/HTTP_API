package com.yan.http_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.service.converter.AddressConverter;
import com.yan.http_api.service.converter.ClientConverter;
import com.yan.http_api.service.dto.ClientDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestClientController {

    //todo: должны быть ещё тесты на Exceptions
// важно, чтобы тесты выполнялись по порядку, потому что в бд добавляются сущности, которые
// удаляются только после того, как все тесты отработают. их id инкрементиться, а мы его также проверяем

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepo mockClientRepo;

    @Autowired
    private ClientConverter clientConverter;

    private List<Client> clientsObj;
    private List<ClientDto> clientDtosObj;

    private List<String> clients;
    private List<String> clientDtos;

    private String objToJsonString(Object o) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(o);
    }
//
//    {
//        ClientConverter clientConverter = new ClientConverter(new AddressConverter());
//
//        clientsObj = new ArrayList<>();
//        clientDtosObj = new ArrayList<>();
//
//        clientsObj.add(new Client("gerasim", "11", "+11",
//                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
//                null));
//        clientsObj.add(new Client("vasilii", "2", "+2",
//                new Address(2, "moscow", "moscow", "blinnaya", 2, 2, 2),
//                null));
//        clientsObj.add(new Client("Polina", "3", "+3",
//                new Address(3, "Freedom", "Hope", "Frosty", 3, 3, 3),
//                null));
//
//        clientDtosObj = clientConverter.entityToDto(clientsObj);
//        clientDtosObj.get(0).setId(1L).getAddress().setId(1L);
//        clientDtosObj.get(1).setId(2L).getAddress().setId(2L);
//        clientDtosObj.get(2).setId(2L).getAddress().setId(2L);//умышленно тот же id, что и у предыдущего!
//
//
////        clientDtosObj.add(clientConverter.entityToDto(clientsObj.get(0)).setId(1L));
////        clientDtosObj.get(0).getAddress().setId(1L);
////
////        clientDtosObj.add(clientConverter.entityToDto(clientsObj.get(1)).setId(2L));
////        clientDtosObj.get(1).getAddress().setId(2L);
////
////        clientDtosObj.add(clientConverter.entityToDto(clientsObj.get(2)).setId(2L));
////        clientDtosObj.get(2).getAddress().setId(2L);
//
//        clients = clientsObj.stream()
//                .map(client -> {
//                    try {
//                        return objToJsonString(client);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException();
//                    }
//                }).collect(Collectors.toList());
//
//        clientDtos = clientDtosObj.stream()
//                .map(dto -> {
//                    try {
//                        return objToJsonString(dto);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException();
//                    }
//                }).collect(Collectors.toList());
//
//    }


    @BeforeEach
    void setUp() {
        mockClientRepo.save(new Client("gerasim", "11", "+11",
                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
                null));
    }


    @Test
    @DisplayName("[Get] Получаем клиента с id = 1, проверяем все поля, кроме address")
    void test1() throws Exception {


        mockMvc.perform(get("/client/get_by_id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("gerasim"))
                .andExpect(jsonPath("$.inn").value("11"))
                .andExpect(jsonPath("$.phone").value("+11"));
    }

    @Test
    @DisplayName("[Get] Получаем клиента с id = 1, проверяем совпадение json")
    void test2() throws Exception {
        mockClientRepo.save(new Client("gerasim", "11", "+11",
                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
                null));

        mockMvc.perform(get("/client/get_by_id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(clientDtos.get(0)));
    }

    @Test
    @DisplayName("[Post] Добавляем клиента (автоматически id = 2), проверяем совпадение json")
    public void test3() throws Exception {
        mockMvc.perform(post("/client/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clients.get(1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(clientDtos.get(1)));
    }

    @Test
    @DisplayName("[Put] Изменяем клиента с id = 2")
    public void test4() throws Exception {
        mockMvc.perform(put("/client/put/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clients.get(2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(clientDtos.get(2)));
    }

    @Test
    @DisplayName("[Get] Получаем всех клиентов")
    public void test5() throws Exception {
//        mockMvc.perform(get("/client/get_all"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(
//                        objToJsonString(List.of(clientDtosObj.get(0), clientDtosObj.get(2)))
//                ));
        mockMvc.perform(get("/client/get_all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[" + clientDtos.get(0) + "," + clientDtos.get(2) + "]"));
    }

    @Test
    @DisplayName("[Delete] Удаляем клиента без заказов")
    public void test6() throws Exception {
        mockMvc.perform(delete("/client/delete/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Клиент успешно удалён"));

        assertFalse(mockClientRepo.existsById(2L));
    }

    @Test
    @DisplayName("[Get] Получаем клиента с именем gerasim, проверяем совпадение json")
    public void test7() throws Exception {
        mockMvc.perform(get("/client/get_by_name/gerasim"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(clientDtos.get(0)));
    }
    //todo: удалить клиента с заказами, проверить, что заказы исчезли


}
