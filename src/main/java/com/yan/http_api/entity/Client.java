package com.yan.http_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле имя не должно быть null")
    @NotEmpty(message = "Поле имя не должно быть пустым")
    private String name;
    private String inn;

    @NotNull(message = "Поле номер телефона не должно быть null")
    @Pattern(regexp = "^\\+?\\d+$", message = "Недопустимые символы в номере телефона и/или недостаточно цифр")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "client_id")//тогда у orderr будет автоматически создаваться fk
    private List<Orderr> orders;

    public Client(String name, String inn, String phone, Address address, List<Orderr> orders) {
        this.name = name;
        this.inn = inn;
        this.phone = phone;
        this.address = address;
        this.orders = orders;
    }
}
