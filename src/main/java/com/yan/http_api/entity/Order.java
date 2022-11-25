package com.yan.http_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private LocalDate creationDate;
    private String description;
    private Integer cost;
}
