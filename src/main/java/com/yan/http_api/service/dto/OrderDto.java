package com.yan.http_api.service.dto;

import com.yan.http_api.entity.Orderr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the {@link Orderr} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDto implements Serializable {
    private Long id;
    private LocalDate creationDate;
    private String descriptionn;
    private Integer cost;
}