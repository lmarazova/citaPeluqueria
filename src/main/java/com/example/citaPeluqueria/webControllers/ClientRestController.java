package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.mapper.AppointmentMapper;
import com.example.citaPeluqueria.mapper.ClientMapper;
import com.example.citaPeluqueria.domain.dtos.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 Controlador REST que gestiona operaciones relacionadas con los clientes.
 Incluye endpoints para obtener usuarios, eliminar citas y actualizar observaciones.
 Este controlador se utiliza principalmente en la interfaz del moderador.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    AppointmentMapper appointMapper;
    @Autowired
    AppointmentService appointmentService;
    private final ClientRepository clientRepository;

    /**
     * Constructor que inyecta el repositorio de clientes.
     *
     * @param clientRepository Repositorio de acceso a datos de clientes.
     */
    public ClientRestController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Obtiene los datos de un cliente específico por su ID.
     *
     * @param id ID del cliente a buscar.
     * @return ResponseEntity con el DTO del cliente si se encuentra, o 404 si no.
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRestDTO> getUserById(@PathVariable Long id){
        Optional<ClientEntity> optionalUser = clientRepository.findById(id);
        if(optionalUser.isPresent()){
            UserRestDTO userRestDTO = clientMapper.toDTO(optionalUser.get());
            return ResponseEntity.ok(userRestDTO);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Obtiene una lista de todos los clientes en formato DTO.
     * Incluye también las citas de cada cliente.
     *
     * @return Lista de usuarios con sus datos y citas.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserRestDTO>getAllUsers(){
        List<ClientEntity>clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> new UserRestDTO(
                        client.getId(),
                        client.getUsername(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getRoles().toString(),
                        client.getComments(),
                        client.isGuest(),
                        appointMapper.toDTOList(new ArrayList<>(client.getAppointments()))
                )).toList();
    }

    /**
     * Elimina una cita de un cliente según su ID, fecha y franja horaria seleccionada.
     * Utilizado por los moderadores desde el panel de administración.
     *
     * @param userId ID del cliente.
     * @param date Fecha de la cita (formato esperado: yyyy-MM-dd).
     * @param selectedHourRange Rango horario de la cita (ej. "10:00-10:15").
     * @param redirectAttributes Atributos para enviar mensajes flash a la vista.
     * @return Redirección a la vista del moderador.
     */
    @PostMapping("/moderator/delete-appointment")
    public String deleteAppointment(@RequestParam Long userId,
                                    @RequestParam String date,
                                    @RequestParam String selectedHourRange,
                                    RedirectAttributes redirectAttributes) {
        appointmentService.deleteByClientDateAndHourRange(userId, date, selectedHourRange);
        redirectAttributes.addFlashAttribute("success", "Cita eliminada correctamente.");
        return "redirect:/moderator"; // O redirige a donde tú necesites
    }

    /**
     * Actualiza los comentarios/observaciones de un cliente.
     * Utilizado desde el panel del moderador.
     *
     * @param userId ID del cliente.
     * @param comments Nuevos comentarios que se guardarán.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la vista del moderador.
     */
    @PostMapping("/moderator/update-comments")
    public String updateComments(@RequestParam Long userId,
                                 @RequestParam String comments,
                                 RedirectAttributes redirectAttributes) {
        Optional<ClientEntity> optionalUser = clientRepository.findById(userId);
        if (optionalUser.isPresent()) {
            ClientEntity user = optionalUser.get();
            user.setComments(comments);
            clientRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Observaciones guardadas correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/moderator";
    }


}
