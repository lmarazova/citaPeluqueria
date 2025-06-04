package com.example.citaPeluqueria.domain.enums;
/**
 * Enum que representa el estado de una franja horaria (slot) dentro del sistema.
 * Se utiliza para indicar si un slot está disponible, reservado o bloqueado.
 */
public enum SlotStatus {
    /**
     * El slot está libre y disponible para ser reservado por un cliente.
     */
    FREE,

    /**
     * El slot ya ha sido reservado o asignado a una cita.
     */
    OCCUPIED,
    BLOCKED;
}
