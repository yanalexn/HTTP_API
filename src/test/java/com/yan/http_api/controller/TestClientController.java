package com.yan.http_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yan.http_api.entity.Address;
import com.yan.http_api.service.dto.ClientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests")
public class TestClientController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Получаем клиента с id = 1, проверяем все поля, кроме address")
    public void test1() throws Exception {
        mockMvc.perform(get("/client/get_by_id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("gerasim"))
                .andExpect(jsonPath("$.inn").value("11"))
                .andExpect(jsonPath("$.phone").value("+11"));
    }

    @Test
    @DisplayName("Получаем клиента с id = 1, проверяем совпадение json")
    public void test2() throws Exception {
        ClientDto clientDto = new ClientDto(1L, "gerasim", "11", "+11",
                new Address(1L, 11, "moscow", "moscow", "kudago", 11, 11, 11));

        mockMvc.perform(get("/client/get_by_id/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(clientDto)));
    }
}
