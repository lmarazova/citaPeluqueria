package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.HairdresserRegisterDTO;
import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
/**
 * Servicio que gestiona la lógica relacionada con los peluqueros del sistema.
 */
public interface HairdresserService {
    /**
     * Registra un nuevo peluquero en el sistema con el rol de MODERATOR.
     *
     * @param hairdresserRegisterDTO datos del peluquero a registrar.
     */
    public void registerHairdresser(HairdresserRegisterDTO hairdresserRegisterDTO);
}
