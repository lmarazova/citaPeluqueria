package com.example.citaPeluqueria.domain.dtos.restDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotRestDTO {
    private Long id;
    private String slotStatus;
    private String clientName;
    private String hairdresserName;
    private String servicePackName;
    private String startHour;
    private String finalHour;
}
