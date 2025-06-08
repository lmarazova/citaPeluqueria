package com.example.citaPeluqueria.domain.dtos.restDTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HairdresserRestDTO {
    private Long id;
    private String username;
    private String email;
    private List<SlotRestDTO> slots;
}
