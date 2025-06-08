package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.mapper.AppointmentRestMapper;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Controlador REST para gestionar las citas {@link AppointmentEntity}.
 * Expone endpoints para obtener citas individuales o todas las citas disponibles.
 */
@Tag(name = "Appointments", description = "Operaciones relacionadas con la gestión de citas")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
    @Autowired
    public AppointmentRestMapper appointmentRestMapper;

    private final AppointmentRepository appointmentRepository;

    /**
     * Constructor que inyecta el repositorio de citas.
     *
     * @param appointmentRepository repositorio para acceder a las citas.
     */
    public AppointmentRestController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Obtiene una cita por su identificador.
     *
     * @param id ID de la cita a buscar.
     * @return {@link ResponseEntity} con el DTO de la cita si existe, o 404 si no se encuentra.
     */
    @Operation(
            summary = "Obtener cita por ID",
            description = "Devuelve los detalles de una cita específica usando su identificador"
    )
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentRestDTO>getAppointmentById(@PathVariable Long id){
        Optional<AppointmentEntity>optionalAppointmentEntity = appointmentRepository.findById(id);
        if(optionalAppointmentEntity.isPresent()){
            AppointmentRestDTO appointmentRestDTO = appointmentRestMapper.toDTO(optionalAppointmentEntity.get());
            return ResponseEntity.ok(appointmentRestDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todas las citas registradas en el sistema.
     *
     * @return lista de {@link AppointmentRestDTO} con los datos esenciales de cada cita.
     */
    @Operation(
            summary = "Obtener todas las citas",
            description = "Devuelve una lista con todas las citas almacenadas en el sistema"
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppointmentRestDTO>getAllAppointments(){
        List<AppointmentEntity>appointmentEntities = appointmentRepository.findAll();
        return appointmentEntities.stream()
                .map(appointment -> new AppointmentRestDTO(
                        appointment.getId(),
                        appointment.getUser().getUsername(),
                        appointment.getService().getDescription(),
                        appointment.getSelectedHourRange(),
                        appointment.getDate()

                )).collect(Collectors.toList());
    }
}
