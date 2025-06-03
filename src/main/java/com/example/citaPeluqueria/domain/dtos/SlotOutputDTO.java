package com.example.citaPeluqueria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotOutputDTO {
    private Long id;
    private String date;
    private String slotStartHour;
    private String slotFinalHour;
    private String hairdresser;
    private String slotStatus;
}
