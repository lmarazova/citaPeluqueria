package com.example.citaPeluqueria.util;

import com.example.citaPeluqueria.domain.enums.SlotStatus;

import java.net.URI;
/**
 * Clase que contiene constantes globales usadas en toda la aplicación.
 *
 * Incluye mensajes de validación, etiquetas de servicios de peluquería,
 * configuración de horas y datos administrativos.
 *
 * Estos valores permiten centralizar textos y configuraciones para evitar
 * hardcodear strings por todo el código.
 */
public class Constants {
    public static final String THE_EMAIL_IS_REQUIRED ="El correo es obligatorio para tu identificación";
    public static final String PASSWORD_IS_REQUIRED = "No te puedes registrar sin elegir contraseña";

    // Etiquetas legibles para los servicios de peluquería
    public static final String CUT= "Corte";
    public static final String COLOR = "Tinte";
    public static final String HIGHLIGHTS = "Mechas";
    public static final String WASH = "Lavado";
    public static final String BLOWDRY = "Secado";
    public static final String STRAIGHTEN = "Planchar";
    public static final String CURL = "Rizar";
    public static final String MASK = "Mascarilla";
    public static final String HYDRATION = "Hidratación";

    // Combinaciones de servicios (ejemplo de etiqueta compuesta)
    public static final String CUT_WASH_BLOWDRY = "Corte + Lavado + Secado";
    public static final String COLOR_WASH_BLOWDRY = "Tinte + Lavado + Secado";
    public static final String HIGHLIGHTS_CUT_WASH = "Mechas + Corte + Lavado";
    public static final String HIGHLIGHTS_CUT_WASH_BLOWDRY = "Mechas + Corte + Lavado + Secado";
    public static final String CURL_BLOWDRY = "Rizar + Secado";
    public static final String COLOR_HIGHLIGHTS_WASH_BLOWDRY = "Tinte + Mechas + Lavado + Secado";
    public static final String CUT_MASK = "Corte + Mascarilla";
    public static final String COLOR_MASK = "Tinte + Mascarilla";
    public static final String FULL_TREATMENT = "Tratamiento Completo";
    public static final String STRAIGHTEN_BLOWDRY = "Planchar + Secado";
    public static final String CUT_HIGHLIGHTS = "Corte + Mechas";
    public static final String CURL_HIGHLIGHTS = "Rizar + Mechas";
    public static final String STRAIGHTEN_HIGHLIGHTS = "Planchar + Mechas";
    public static final String HYDRATION_BLOWDRY = "Hidratación + Secado";

    // Configuraciones para agenda y usuarios admin
    public static final int SLOT_INTERVAL = 15;

    public static final int START_HOUR = 10;
    public static final int WORKED_HOURS = 3;
    public static final long DAYS_TOTAL_AVAILABLE = 7;
    public static final String ADMIN_NAME = "admin";
    public static final String ADMIN_EMAIL = "admin@domain.com";
    public static final String ADMIN_PHONE = "123456789";
    public static final CharSequence ADMIN_PASSWORD = "admin123";
    public static final String ACTIVE = "active";
    public static final String PASSIVE = "passive";
    public static final String CUSTOM = "Personalizado";
    public static final String IMG_PATH = "scr/mai/resources/static/img";
}
