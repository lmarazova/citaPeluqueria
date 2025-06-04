package com.example.citaPeluqueria.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
/**
 * Clase utilitaria para formatear rangos de fecha-hora a solo horas (hh:mm - hh:mm).
 */
public class HourFormatter {

    /**
     * Convierte un conjunto de rangos de fecha-hora (formato completo) a un conjunto ordenado de solo horas.
     *
     * Ejemplo de entrada: ["2025-06-01T09:00 - 2025-06-01T09:30"]
     * Ejemplo de salida: ["09:00 - 09:30"]
     *
     * @param dateTimeRanges conjunto de cadenas con rangos de fecha y hora (ISO-8601).
     * @return conjunto ordenado de rangos de horas (hh:mm - hh:mm).
     */
    public static Set<String> formatToHourOnly(Set<String> dateTimeRanges) {
        return dateTimeRanges.stream()
                .map(range -> {
                    String[] parts = range.split(" - ");
                    String start = parts[0].substring(11, 16); // hh:mm
                    String end = parts[1].substring(11, 16);   // hh:mm
                    return start + " - " + end;
                })
                .collect(Collectors.toCollection(TreeSet::new)); // Mantiene orden
    }
}
