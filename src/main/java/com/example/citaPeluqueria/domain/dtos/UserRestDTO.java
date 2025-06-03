package com.example.citaPeluqueria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRestDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String comments;
    private boolean guest;
    private List<AppointmentDTO> appointmentDTOS;
}

