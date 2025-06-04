package com.example.citaPeluqueria.domain.enums;

import com.example.citaPeluqueria.util.Constants;
import lombok.Getter;

import java.util.List;
/**
 * Enum que representa los diferentes paquetes de servicios disponibles.
 *
 * Cada paquete tiene un nombre para mostrar, una duración total en minutos,
 * y un patrón de franjas horarias (slots) que indica la disponibilidad o
 * el estado esperado de cada segmento del servicio.
 *
 * Ejemplo: un paquete puede durar 45 minutos y tener tres slots libres consecutivos.
 */
@Getter
public enum ServicePackageEnum {
    CUT_WASH_BLOWDRY(Constants.CUT_WASH_BLOWDRY, 45,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE)),

    COLOR_WASH_BLOWDRY(Constants.COLOR_WASH_BLOWDRY, 60,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE)),

    HIGHLIGHTS_CUT_WASH(Constants.HIGHLIGHTS_CUT_WASH, 75,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE)),

    HIGHLIGHTS_CUT_WASH_BLOWDRY(Constants.HIGHLIGHTS_CUT_WASH_BLOWDRY, 90,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE, SlotStatus.FREE)),

    CURL_BLOWDRY(Constants.CURL_BLOWDRY, 60,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE)),

    COLOR_HIGHLIGHTS_WASH_BLOWDRY(Constants.COLOR_HIGHLIGHTS_WASH_BLOWDRY, 90,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE, SlotStatus.FREE)),

    CUT_MASK(Constants.CUT_MASK, 45,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.FREE)),

    COLOR_MASK(Constants.COLOR_MASK, 60,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE)),

    FULL_TREATMENT(Constants.FULL_TREATMENT, 105,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE)),

    STRAIGHTEN_BLOWDRY(Constants.STRAIGHTEN_BLOWDRY, 60,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE)),

    CUT_HIGHLIGHTS(Constants.CUT_HIGHLIGHTS, 75,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE)),

    CURL_HIGHLIGHTS(Constants.CURL_HIGHLIGHTS, 75,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE, SlotStatus.FREE)),

    STRAIGHTEN_HIGHLIGHTS(Constants.STRAIGHTEN_HIGHLIGHTS, 75,
            List.of(SlotStatus.FREE, SlotStatus.OCCUPIED, SlotStatus.OCCUPIED, SlotStatus.FREE, SlotStatus.FREE)),

    HYDRATION_BLOWDRY(Constants.HYDRATION_BLOWDRY, 45,
            List.of(SlotStatus.FREE, SlotStatus.FREE, SlotStatus.FREE));
    /**
     * Nombre para mostrar del paquete de servicio.
     */
    private final String displayName;
    /**
     * Duración total del servicio en minutos.
     */
    private final int durationMinutes;
    /**
     * Patrón de estados para las franjas horarias que componen el servicio.
     * Indica qué slots están libres, ocupados, etc., y su secuencia.
     */
    private final List<SlotStatus> slotPattern;
    /**
     * Constructor del enum.
     *
     * @param displayName Nombre descriptivo del paquete.
     * @param durationMinutes Duración total en minutos.
     * @param slotPattern Lista que representa el estado de cada franja del servicio.
     */
    ServicePackageEnum(String displayName, int durationMinutes, List<SlotStatus> slotPattern) {

        this.displayName = displayName;

        this.durationMinutes = durationMinutes;

        this.slotPattern = slotPattern;
    }
}
