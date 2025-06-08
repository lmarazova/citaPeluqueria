package com.example.citaPeluqueria.domain.dtos.restDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackRestDTO {
    private Long id;
    private String description;
    private boolean isActive;
    private double price;
    private int totalDuration;
}
