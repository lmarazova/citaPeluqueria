package com.example.citaPeluqueria.domain.dtos.restDTOS;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRestDTO {
    private Long id;
    private String clientUsername;
    private String description;
    private String selectedHourRange;
    private String date;
}
