package com.yan.http_api;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.entity.Orderr;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.repo.OrderRepo;
import com.yan.http_api.service.converter.ClientConverter;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.yan.http_api.JsonConverter.objToJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestClientController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ClientConverter clientConverter;

    private List<Client> clients;

    @BeforeEach
    void initEach() {
        clients = new ArrayList<>();

        clients.add(new Client("vasilii", "2", "+2",
                new Address(2, "moscow", "moscow", "blinnaya", 2, 2, 2),
                List.of(new Orderr(LocalDate.of(2022, 11, 15), "посылка", 156),
                        new Orderr(LocalDate.of(2022, 11, 17), "бандероль", 999))));
        clients.add(new Client("gerasim", "11", "+11",
                new Address(11, "moscow", "moscow", "kudago", 11, 11, 11),
                null));
        clients.add(new Client("Polina", "3", "+3",
                new Address(3, "Freedom", "Hope", "Frosty", 3, 3, 3),
                null));

        clientRepo.saveAll(clients);
    }

    @Test
    @DisplayName("[Get] Получаем клиента с id = 2")
    void shouldReturn2ndClientJson() throws Exception {
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
    @DisplayName("[Get] Получаем клиента с именем vasilii, проверяем совпадение json")
    public void shouldReturnClientByName() throws Exception {
        mockMvc.perform(get("/client/get_by_name/vasilii"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(clientConverter.entityToDto(clients.get(0)))));
    }

    @Test
    @DisplayName("[Get] Получаем всех клиентов")
    public void shouldReturnAllClients() throws Exception {
        mockMvc.perform(get("/client/get_all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(clientConverter.entityToDto(clients))));
    }

    @Test
    @DisplayName("[Post] Добавляем клиента (будет id = 4), проверяем совпадение json")
    void shouldCreate4thClient() throws Exception {
        Client client = new Client("Mockito", "4", "+4",
                new Address(4, "Saint P", "Saint P", "WarChest", 4, 4, 4),
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
    }

    @Test
    @DisplayName("[Delete] Удаляем клиента с id=2 без заказов")
    void shouldDeleteWithoutOrders() throws Exception {
        mockMvc.perform(delete("/client/delete/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Клиент успешно удалён"));

        assertFalse(clientRepo.existsById(2L));
    }

    @Test
    @DisplayName("[Delete] Удаляем клиента с id=1 со всеми его заказами")
    void shouldDeleteWithOrders() throws Exception {
        assertTrue(orderRepo.existsById(1L));
        assertTrue(orderRepo.existsById(2L));

//        System.out.println(new OrderConverter().entityToDto(orderRepo.findAll()));

        mockMvc.perform(delete("/client/delete/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Клиент успешно удалён"));

//        System.out.println(new OrderConverter().entityToDto(orderRepo.findAll()));

        assertFalse(orderRepo.existsById(1L));
        assertFalse(orderRepo.existsById(2L));
    }

    @Test
    @DisplayName("[Get] Пытаемся получить клиента с несуществующим id = 10")
    void shouldThrowClientIdExceptionMessage() throws Exception {
        mockMvc.perform(get("/client/get_by_id/10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Клиента с таким id нет в базе"));
    }

    @Test
    @DisplayName("[Get] Пытаемся получить клиента с несуществующим именем Gendy Tartakovsky")
    public void shouldThrowClientNameExceptionMessage() throws Exception {
        mockMvc.perform(get("/client/get_by_name/Gendy%20Tartakovsky"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Клиента с таким именем нет в базе"));
    }

    @Test
    @DisplayName("[Post] Пытаемся добавить клиента без имени (null)")
    void shouldThrowNullNameExceptionMessage() throws Exception {
        Client client = new Client(null, "4", "+4",
                new Address(4, "Saint P", "Saint P", "WarChest", 4, 4, 4),
                null);

        mockMvc.perform(post("/client/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Поле имя не должно быть null"));

        assertFalse(clientRepo.existsById(4L));
    }

    @Test
    @DisplayName("[Put] Пытаемся изменить клиента, дав пустое имя")
    void shouldThrowEmptyNameExceptionMessage() throws Exception {
        Client client = new Client("", "4", "+4",
                new Address(4, "Saint P", "Saint P", "WarChest", 4, 4, 4),
                null);

        mockMvc.perform(put("/client/put/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Поле имя не должно быть пустым"));

        assertEquals("gerasim", clientRepo.findById(2L).get().getName());
    }

    @Test
    @DisplayName("[Post] Пытаемся добавить клиента без телефона (null)")
    void shouldThrowNullPhoneExceptionMessage() throws Exception {
        Client client = new Client("George", "4", null,
                new Address(4, "Saint P", "Saint P", "WarChest", 4, 4, 4),
                null);

        mockMvc.perform(post("/client/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Поле номер телефона не должно быть null"));

        assertFalse(clientRepo.existsById(4L));
    }

    @Test
    @DisplayName("[Post] Пытаемся добавить клиента с недопустимым номером телефона")
    void shouldThrowWrongFormatPhoneExceptionMessage() throws Exception {
        Client client = new Client("George", "4", "abcd",
                new Address(4, "Saint P", "Saint P", "WarChest", 4, 4, 4),
                null);

        mockMvc.perform(post("/client/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(client)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Недопустимые символы в номере телефона и/или недостаточно цифр"));

        assertFalse(clientRepo.existsById(4L));
    }
}
