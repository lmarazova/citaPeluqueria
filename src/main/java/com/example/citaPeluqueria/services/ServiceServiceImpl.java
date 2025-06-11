package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.ServiceFullDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * Implementación de {@link ServiceService} que gestiona lógica relacionada con los servicios (paquetes).
 */
@Service
public class ServiceServiceImpl implements ServiceService{
    private final ObjectMapper mapper;
    private final ServiceRepository serviceRepository;
    @Autowired
    private SlotService slotService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HairServiceService hairServiceService;

    public ServiceServiceImpl(ObjectMapper mapper,
                              ServiceRepository serviceRepository) {
        this.mapper = mapper;
        this.serviceRepository = serviceRepository;
    }

    /**
     * Obtiene un paquete de servicios por su ID.
     *
     * @param packageId ID del paquete.
     * @return Entidad del paquete si existe, o null.
     */
    @Override
    public ServiceEntity getById(Long packageId) {

        return serviceRepository.findById(packageId).orElse(null);
    }

    /**
     * Genera una etiqueta legible a partir de los servicios seleccionados,
     * concatenando sus etiquetas individuales.
     *
     * @param selectedServices Lista de mapas con claves/valores de servicios.
     * @return Cadena con etiquetas concatenadas separadas por "+".
     */
    @Override
    public String buildLabelFromSelectedServices(List<Map<String, String>> selectedServices) {
        return selectedServices.stream()
                .map(service -> service.get("label"))
                .collect(Collectors.joining(" + "));
    }

    /**
     * Actualiza el precio y estado de disponibilidad de un paquete de servicios.
     *
     * @param id ID del paquete.
     * @param price Nuevo precio.
     * @param isActive Nuevo estado (activo o no).
     */
    @Override
    public void updateService(Long id, double price, boolean isActive) {
        serviceRepository.findById(id).ifPresent(service -> {
            service.setPrice(price);
            service.setActive(isActive);
            serviceRepository.save(service);
        });
    }

    /**
     * Procesa y guarda un paquete de servicios completo, incluyendo:
     * decodificación de JSON, cálculo de duración, almacenamiento de imagen
     * y persistencia del paquete en la base de datos.
     *
     * @param serviceJson JSON con los servicios individuales seleccionados.
     * @param blocksJson JSON con los bloques de tiempo seleccionados.
     * @param price Precio total del paquete.
     * @param photo Imagen asociada al paquete.
     * @throws IOException si ocurre un error al guardar la imagen.
     */
    @Override
    public void processServicePack(String serviceJson, String blocksJson, double price, MultipartFile photo) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, String>> selectedServices = mapper.readValue(serviceJson, new TypeReference<>() {});
        List<Map<String, String>> blocksList = mapper.readValue(blocksJson, new TypeReference<>() {});
        String servicePackName = buildLabelFromSelectedServices(selectedServices);

        List<SlotStatus> slotStatusList = slotService.convertBlockTypesToSlotStatuses(blocksList);
        int totalTime = slotStatusList.size() * 15;

        savePhoto(photo, servicePackName);
        saveServicePack(selectedServices, servicePackName, price, totalTime);
    }

    /**
     * Guarda una imagen del paquete en una ruta fija del sistema de archivos.
     *
     * @param photo Archivo de imagen recibido.
     * @param servicePackName Nombre base para el archivo guardado.
     * @throws IOException si falla la transferencia del archivo.
     */
    @Override
    public void savePhoto(MultipartFile photo, String servicePackName) throws IOException {
        String uploadDir = "C:\\Users\\lyuba\\OneDrive\\Escritorio\\SPRING ADVANCED\\FINAL PROJECT\\citaPeluquería\\src\\main\\resources\\static\\img\\";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        File destFile = new File(uploadFolder, servicePackName + ".png");
        photo.transferTo(destFile);
        System.out.println("Archivo guardado como: " + destFile.getAbsolutePath());
    }

    /**
     * Guarda en la base de datos un nuevo paquete de servicios.
     *
     * @param selectedServices Lista de servicios seleccionados.
     * @param servicePackName Nombre/etiqueta del paquete.
     * @param price Precio total del paquete.
     * @param totalTime Duración total del paquete en minutos.
     */
    @Override
    public void saveServicePack(List<Map<String, String>> selectedServices, String servicePackName, double price, int totalTime) {
        ServiceFullDTO serviceFullDTO = new ServiceFullDTO();
        serviceFullDTO.setDescription(servicePackName);
        serviceFullDTO.setPrice(price);
        serviceFullDTO.setTotalDuration(totalTime);

        List<String> labels = selectedServices.stream()
                .map(s -> s.get("label"))
                .filter(Objects::nonNull)
                .toList();

        ServiceEntity serviceEntity = modelMapper.map(serviceFullDTO, ServiceEntity.class);
        List<HairService> hairServices = labels.stream()
                .map(hairServiceService::fromLabel)
                .collect(Collectors.toList());

        serviceEntity.setServices(hairServices);
        serviceRepository.save(serviceEntity);
    }


}
