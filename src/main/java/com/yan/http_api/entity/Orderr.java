package com.yan.http_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
public class Orderr {
/*There is a reason for such strange names: ordeRR and descriptioNN - it is because order and description are reserved words
in mysql so I can't use them. "Funny" enough that I spent the whole day searching for this bug and h    ere it is.*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate creationDate;
    private String descriptionn;
    private Integer cost;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "client_id")
    private Client client;
}
