package com.example.citaPeluqueria.domain.dtos.restDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBasicRestDTO {
    private Long id;
    private String serviceName;
}
