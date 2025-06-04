package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Repositorio para gestionar las entidades de {@link SlotEntity}.
 * Proporciona métodos para consultar, filtrar y eliminar slots
 * (franjas horarias) según criterios específicos como fechas, clientes y servicios.
 */
@Repository
public interface SlotRepository extends JpaRepository<SlotEntity, Long> {

    /**
     * Busca todos los slots disponibles (FREE) que empiecen dentro de un rango horario
     * y que estén asociados a un servicio específico.
     *
     * @param startDate inicio del rango de fecha y hora
     * @param endDate fin del rango de fecha y hora
     * @param service servicio asociado al slot
     * @return lista de slots disponibles en ese rango con ese servicio
     */
    List<SlotEntity> findAvailableSlotsByStartHourBetweenAndService(LocalDateTime startDate, LocalDateTime endDate, ServiceEntity service);


    /**
     * Devuelve todos los slots que empiecen dentro de un rango de fecha y hora.
     * Normalmente usado para buscar todos los slots de un día.
     *
     * @param startOfDay inicio del rango (p.ej. 00:00)
     * @param endOfDay fin del rango (p.ej. 23:59)
     * @return lista de slots en ese rango
     */
    List<SlotEntity> findByStartHourBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);


    /**
     * Devuelve todos los slots ordenados por hora para una fecha específica.
     * Usa una función SQL para filtrar por la parte de la fecha (ignorando la hora).
     *
     * @param date fecha deseada (sin hora)
     * @return lista de slots para esa fecha, ordenados por hora de inicio
     */
    @Query("SELECT s FROM SlotEntity s WHERE FUNCTION('DATE', s.startHour) = :date ORDER BY s.startHour ASC")
    List<SlotEntity> findAllByStartHourDateOrderByStartHourAsc(@Param("date") LocalDate date);

    /**
     * Busca todos los slots asignados a un cliente específico para un servicio dado.
     * Útil para evitar duplicaciones o verificar si un usuario ya tiene cita para ese servicio.
     *
     * @param user cliente
     * @param service servicio
     * @return lista de slots reservados por ese cliente para ese servicio
     */
    List<SlotEntity> findByClientAndService(UserEntity user, ServiceEntity service);

    /**
     * Elimina todos los slots cuya hora de inicio esté dentro del rango proporcionado.
     * Normalmente usado para limpieza de slots antiguos o reprogramación masiva.
     *
     * @param startOfDay inicio del rango
     * @param endOfDay fin del rango
     */
    void deleteByStartHourBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
