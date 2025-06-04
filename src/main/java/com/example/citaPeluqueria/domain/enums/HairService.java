package com.example.citaPeluqueria.domain.enums;

import com.example.citaPeluqueria.util.Constants;
import lombok.Getter;

import java.util.Arrays;
/**
 * Enum que define los tipos de servicios de peluquería disponibles por defecto.
 * Cada valor tiene una etiqueta (label) legible que puede usarse para mostrar en UI.
 */
@Getter
public enum HairService {
        CUT(Constants.CUT),
        COLOR(Constants.COLOR),
        HIGHLIGHTS(Constants.HIGHLIGHTS),
        WASH(Constants.WASH),
        BLOWDRY(Constants.BLOWDRY),
        STRAIGHTEN(Constants.STRAIGHTEN),
        CURL(Constants.CURL),
        MASK(Constants.MASK),
        HYDRATION(Constants.HYDRATION),
        /**
         * Valor especial que representa un servicio personalizado.
         * Se usa cuando un administrador crea un nuevo servicio que no está
         * en la lista predefinida, y su etiqueta personalizada se guarda en otro campo.
         */
        CUSTOM(Constants.CUSTOM);
        /**
         * Etiqueta legible para el servicio.
         */
        private final String label;

        HairService(String label) {
                this.label = label;
        }



}
