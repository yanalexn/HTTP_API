package com.yan.http_api;

import com.yan.http_api.entity.Address;
import com.yan.http_api.entity.Client;
import com.yan.http_api.entity.Orderr;
import com.yan.http_api.repo.ClientRepo;
import com.yan.http_api.repo.OrderRepo;
import com.yan.http_api.service.converter.ClientConverter;
import com.yan.http_api.service.converter.OrderConverter;
import com.yan.http_api.service.dto.OrderDto;
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
public class TestOrderController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ClientConverter clientConverter;

    @Autowired
    private OrderConverter orderConverter;

    private Client client;
    private List<Orderr> orders;

    {
        client = new Client("Wachovsky", null, "+1", new Address(), null);
        orders = new ArrayList<>();

        orders.add(new Orderr(LocalDate.of(2022, 11, 15), "??????????????", 156));
        orders.add(new Orderr(LocalDate.of(2022, 11, 17), "??????????????????", 999));
        orders.add(new Orderr(LocalDate.of(2022, 11, 19), "????????????", 55));

        client.setOrders(orders);
    }

    @BeforeEach
    void initEach() {
        clientRepo.save(client);//???????????? ?????????????????????????? ????????????????????
    }

    @Test
    @DisplayName("[Get] ???????????????? ?????????? ?? id=3")
    void shouldReturn3rdOrder() throws Exception {
        mockMvc.perform(get("/order/get_by_id/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(orderConverter.entityToDto(orders.get(2)))));
    }

    @Test
    @DisplayName("[Get] ???????????????? ?????? ???????????? ?? ?????????????? ?? id=1")
    void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/order/get_all_for_client/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(orderConverter.entityToDto(orders))));
    }

    @Test
    @DisplayName("[Post] ?????????????????? ?????????? ?????????????? ?? id=1 (?? ???????????? ?????????? id=4)")
    void shouldCreate4thOrder() throws Exception {
        Orderr order = new Orderr(LocalDate.of(2022, 11, 29), "????????", 4500);

        OrderDto orderDto = orderConverter.entityToDto(order).setId(4L);

        mockMvc.perform(post("/order/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(List.of(order))))//?????????? ?????????????????????? ?????????????????? ???? 1 ??????????, ?? ?????????? ????????????
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(List.of(orderDto))));

        assertTrue(orderRepo.existsById(4L));
        assertEquals("????????", orderRepo.findById(4L).get().getDescriptionn());
        assertEquals(1L, orderRepo.findById(4L).get().getClient().getId());
    }

    @Test
    @DisplayName("[Put] ???????????????? ?????????? ?? id = 1")
    void shouldChange1stOrder() throws Exception {
        Orderr order = new Orderr(LocalDate.of(2022, 11, 29), "????????", 4500);

        OrderDto orderDto = orderConverter.entityToDto(order).setId(1L);

        mockMvc.perform(put("/order/put/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objToJsonString(order)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objToJsonString(orderDto)));

        assertEquals(LocalDate.of(2022, 11, 29), orderRepo.findById(1L).get().getCreationDate());
    }

    @Test
    @DisplayName("[Delete] ?????????????? ?????????? ?? id=2")
    void shouldDelete2ndOrder() throws Exception {
        mockMvc.perform(delete("/order/delete/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("?????????? ?????????????? ????????????"));

        assertFalse(clientRepo.existsById(2L));
    }

    @Test
    @DisplayName("[Delete] ???????????????? ?????????????? ?????????? ?? ???????????????????????????? id=20")
    void shouldThrowOrderIdExceptionMessage() throws Exception {
        mockMvc.perform(delete("/order/delete/20"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("???????????? ?? ?????????? id ?????? ?? ????????"));
    }
}
