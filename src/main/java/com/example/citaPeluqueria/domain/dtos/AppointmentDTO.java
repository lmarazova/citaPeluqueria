package com.example.citaPeluqueria.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
/**
 * DTO (Data Transfer Object) para representar los datos necesarios al crear o mostrar una cita (Appointment).
 * Esta clase es utilizada para intercambiar datos entre la capa de presentación y la lógica del backend,
 * sin exponer directamente las entidades.
 */
@Getter
@Setter
@NoArgsConstructor
@Component
public class AppointmentDTO {
    /**
     * Fecha de la cita en formato de texto (por ejemplo, "2025-06-03").
     */
    private String Date;

    /**
     * Franja horaria seleccionada para la cita (por ejemplo, "14:00 - 15:30").
     */
    private String selectedHourRange;

    /**
     * Servicio solicitado en la cita.
     * Contiene la información del paquete de servicio elegido.
     */
    private ServiceDTO service;
}
