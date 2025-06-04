package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.entities.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
/**
 * Servicio para gestionar las citas de los usuarios.
 */
public interface AppointmentService {

    /**
     * Devuelve todas las citas asociadas a un cliente.
     *
     * @param user cliente cuyas citas se desean recuperar.
     * @return lista de citas del cliente.
     */
    public List<AppointmentEntity> getAppointmentsByUser(ClientEntity user);

    /**
     * Prepara el modelo para mostrar la vista de selección de horas para una cita.
     *
     * @param model modelo de datos de la vista.
     * @param packageId ID del servicio seleccionado.
     * @param date fecha seleccionada.
     * @param user cliente actual.
     */
    void prepareHourSelectionView(Model model, Long packageId, String date, ClientEntity user);

    /**
     * Crea una cita si hay disponibilidad para la combinación de franjas seleccionada.
     *
     * @param selectedHourRange rango horario elegido por el usuario.
     * @param packageId ID del paquete o servicio.
     * @param date fecha seleccionada.
     * @param user cliente que realiza la reserva.
     * @return true si se creó exitosamente; false si no hubo disponibilidad.
     */
    boolean createAppointment(String selectedHourRange, Long packageId, String date, ClientEntity user);

    /**
     * Elimina una cita específica si pertenece al usuario autenticado.
     *
     * @param appointmentId ID de la cita a eliminar.
     * @param principal usuario autenticado (Spring Security).
     */
    void deleteClientAppointment(Long appointmentId, Principal principal);

    /**
     * Elimina una cita según el cliente, fecha y rango horario.
     *
     * @param userId ID del cliente.
     * @param date fecha de la cita.
     * @param selectedHourRange rango horario de la cita.
     */
    void deleteByClientDateAndHourRange(Long userId, String date, String selectedHourRange);
}
