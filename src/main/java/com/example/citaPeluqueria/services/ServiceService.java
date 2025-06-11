package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.CustomServicePackageDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * Servicio para operaciones relacionadas con entidades de servicios (paquetes).
 */
public interface ServiceService {


    /**
     * Obtiene un servicio por su identificador.
     *
     * @param packageId El ID del paquete de servicios.
     * @return La entidad del servicio correspondiente, o {@code null} si no se encuentra.
     */
    ServiceEntity getById(Long packageId);

    /**
     * Construye una etiqueta combinada a partir de los servicios seleccionados.
     *
     * @param selectedServices Lista de mapas que contienen claves como "label".
     * @return Una cadena que representa la combinación de servicios (por ejemplo, "Corte + Tinte").
     */
    String buildLabelFromSelectedServices(List<Map<String, String>> selectedServices);

    /**
     * Actualiza el precio y el estado de un servicio.
     *
     * @param id       El ID del servicio.
     * @param price    El nuevo precio.
     * @param isActive Si el servicio está activo o no.
     */
    void updateService(Long id, double price, boolean isActive);

    void processServicePack(String serviceJson, String blocksJson, double price, MultipartFile photo) throws IOException;

    void savePhoto(MultipartFile photo, String servicePackName) throws IOException;

    void saveServicePack(List<Map<String, String>> selectedServices, String servicePackName, double price, int totalTime);
}
