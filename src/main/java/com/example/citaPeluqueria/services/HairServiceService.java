package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.enums.HairService;
/**
 * Servicio que proporciona utilidades relacionadas con el enum {@link HairService}.
 */
public interface HairServiceService {

    /**
     * Obtiene una instancia de {@link HairService} a partir de su etiqueta (label).
     *
     * @param label La etiqueta del servicio de peluquería (por ejemplo, "Corte", "Tinte").
     * @return El valor del enum {@link HairService} correspondiente, o {@code null} si no hay coincidencia.
     */
    HairService fromLabel(String label);
}
