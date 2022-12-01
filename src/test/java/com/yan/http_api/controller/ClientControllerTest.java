package com.yan.http_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.service.converter.ClientConverter;
import com.yan.http_api.service.dto.AddressDto;
import com.yan.http_api.service.dto.ClientDto;
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

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ClientConverter clientConverter;

    private final List<Client> clients;

    {
        clients = new ArrayList<>();
        clients.add(new Client("vasilii", "2", "+2",
                new Address(2, "moscow", "moscow", "blinnaya", 2, 2, 2),
                null));
        clients.add(new Client("gerasim", "11", "+11",
                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
                null));
        clients.add(new Client("Polina", "3", "+3",
                new Address(3, "Freedom", "Hope", "Frosty", 3, 3, 3),
                null));
    }

    @BeforeEach
    void initEach() {
        clientRepo.saveAll(clients);
    }

    //преобразует объект в JSON
    private String objToJsonString(Object o) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(o);
    }

    @Test
    @DisplayName("[Get] Получаем клиента с id = 2")
    void shouldReturn2ndClientJson() throws Exception {
        System.out.println(clientConverter.entityToDto(clients.get(1)));

        mockMvc.perform(get("/client/get_by_id/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("gerasim"))
                .andExpect(jsonPath("$.inn").value("11"))
                .andExpect(jsonPath("$.phone").value("+11"))
                .andExpect(content().json(objToJsonString(clientConverter.entityToDto(clients.get(1)))));
    }

    @Test
    @DisplayName("[Post] Добавляем клиента (будет id=4), проверяем совпадение json")
    void shouldCreate4thClient() throws Exception {
        Client client = new Client("Mockito", "4", "+4",
                new Address(4, "Saint P", "Saint P", "Kibitka", 4, 4, 4),
                null);

        ClientDto clientDto = clientConverter.entityToDto(client).setId(4L);
        clientDto.getAddress().setId(4L);

        mockMvc.perform(post("/client/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(clientDto)));

        assertTrue(clientRepo.existsById(4L));
        assertEquals("Mockito", clientRepo.findById(4L).get().getName());
    }

    @Test
    @DisplayName("[Put] Изменяем клиента с id = 3")
    void shouldChange3rdClient() throws Exception {
        Client client = new Client("Hoffner", "3", "+3",
                new Address(3, "Saint P", "Saint P", "Vitrazh", 3, 3, 3),
                null);

        ClientDto clientDto = clientConverter.entityToDto(client).setId(3L);
        clientDto.getAddress().setId(3L);

        mockMvc.perform(put("/client/put/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(clientDto)));

        assertEquals("Hoffner", clientRepo.findById(3L).get().getName());

//        /////////////////////////////////
//        mockMvc.perform(get("/client/get_all"))
//                .andDo(print());
    }
}
