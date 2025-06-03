package com.example.citaPeluqueria.webControllers;



import com.example.citaPeluqueria.domain.dtos.ServiceFullDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.HairServiceService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.SlotService;
import com.example.citaPeluqueria.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CreateServiceController {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private SlotService slotService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private HairServiceRepository hairServiceRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private HairServiceService hairServiceService;

    @GetMapping("/create-service")
    public String showCreateService(@RequestParam(required = false) String serviceJson, Model model){
        /*List<HairServiceEntity>services = hairServiceRepository.findByHairServiceNot(HairService.CUSTOM);*/
        List<HairServiceEntity>services = hairServiceRepository.findAll();
        model.addAttribute("basicServices", services);
        model.addAttribute("serviceJson", serviceJson);
        return "create-service";
    }
    @PostMapping("/show-blocks")
    public String showBlocks(@RequestParam("blocksJson") String blocksJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> blocks = mapper.readValue(blocksJson, new TypeReference<>() {});
        List<SlotStatus>slotStatusList = slotService.convertBlockTypesToSlotStatuses(blocks);
        slotStatusList.forEach(System.out::println);
        return "redirect:/create-service";

    }
    @PostMapping("/show-services")
    public String showServices(@RequestParam("serviceJson") String serviceJson, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Parsear servicios seleccionados
        List<Map<String, String>> selectedServices = mapper.readValue(serviceJson, new TypeReference<>() {});
        selectedServices.forEach(service -> System.out.println("Servicio ID: " + service.get("id") + ", Label: " + service.get("label")));
        String servicePackName = serviceService.buildLabelFromSelectedServices(selectedServices);
        System.out.println(servicePackName);
        // Aquí puedes guardar o hacer lo que necesites con ambos datos
        model.addAttribute("packName", servicePackName);
        model.addAttribute("serviceJson", serviceJson);
        return "create-service";
    }

    @PostMapping("/services/add")
    public String addServiceCustom(@RequestParam("label")String label, Model model){
        System.out.println("Label recibido: " + label);
        if(label != null && !label.trim().isEmpty()){
            HairServiceEntity customService = new HairServiceEntity();
            customService.setHairService(HairService.CUSTOM);
            customService.setCustomLabel(label.trim());
            hairServiceRepository.save(customService);

            List<HairServiceEntity> allServices = hairServiceRepository.findAll();
            model.addAttribute("basicServices", allServices);
        }
        return "redirect:/create-service";
    }
    @DeleteMapping("/delete-custom-service/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCustomService(@PathVariable Long id) {
        Optional<HairServiceEntity> optional = hairServiceRepository.findById(id);

        if (optional.isPresent()) {
            HairServiceEntity service = optional.get();
            if (service.getHairService() == HairService.CUSTOM) {
                hairServiceRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
    @PostMapping("/upload-photo")

    public String handlePhotoUpload(@RequestParam("photo") MultipartFile photo,
                                    @RequestParam("serviceJson") String serviceJson,
                                    Model model) throws IOException {
        System.out.println("serviceJson recibido en upload-photo: " + serviceJson);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> selectedServices = mapper.readValue(serviceJson, new TypeReference<>(){});
        String servicePackName = serviceService.buildLabelFromSelectedServices(selectedServices);
        // Carpeta destino absoluta
        String uploadDir = "C:\\Users\\lyuba\\OneDrive\\Escritorio\\SPRING ADVANCED\\FINAL PROJECT\\citaPeluquería\\src\\main\\resources\\static\\img\\";

        // Crear carpeta si no existe
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // Nombre nuevo del archivo (serviceJson + .png)
        String newFileName = servicePackName + ".png";

        // Crear archivo destino con ruta completa
        File destFile = new File(uploadFolder, newFileName);

        // Guardar archivo
        photo.transferTo(destFile);

        System.out.println("Archivo guardado en: " + destFile.getAbsolutePath());

        //para que se guarde la vista despues de subir la imagen
        List<HairServiceEntity> services = hairServiceRepository.findAll();
        model.addAttribute("basicServices", services);
        model.addAttribute("serviceJson", serviceJson);
//
        return "create-service"; // O la vista que prefieras
    }

    @PostMapping("/submit-price")
    public String submitPrice(@RequestParam("price") String price) {
        System.out.println("PRECIO RECIBIDO: " + price);
        // Aquí puedes redirigir o devolver una vista
        return "redirect:/create-service"; // Ajusta al nombre de tu vista o ruta
    }
    @PostMapping("/submit-all")
    public String submitAll(
            @RequestParam("serviceJson") String serviceJson,
            @RequestParam("blocksJson") String blocksJson,
            @RequestParam("price") double price,
            @RequestParam("photo") MultipartFile photo,
            Model model
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Parseo
        List<Map<String, String>> selectedServices = mapper.readValue(serviceJson, new TypeReference<>() {});
        List<Map<String, String>> blocksList = mapper.readValue(blocksJson, new TypeReference<>() {});
        String servicePackName = serviceService.buildLabelFromSelectedServices(selectedServices);

        List<SlotStatus> slotStatusList = slotService.convertBlockTypesToSlotStatuses(blocksList);
        int totalTime = slotStatusList.size() * 15;

        // Guardar imagen exactamente igual
        String uploadDir = "C:\\Users\\lyuba\\OneDrive\\Escritorio\\SPRING ADVANCED\\FINAL PROJECT\\citaPeluquería\\src\\main\\resources\\static\\img\\";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        String newFileName = servicePackName + ".png";
        File destFile = new File(uploadFolder, newFileName);
        photo.transferTo(destFile);

        System.out.println("Archivo guardado como: " + destFile.getAbsolutePath());
        // Logs
        System.out.println("Nombre paquete servicio: " + servicePackName);
        System.out.println("Precio: " + price);
        System.out.println("Tiempo total: " + totalTime);

        // Para mantener la vista
        List<HairServiceEntity> services = hairServiceRepository.findAll();
        model.addAttribute("basicServices", services);
        model.addAttribute("serviceJson", serviceJson);

        ServiceFullDTO serviceFullDTO = new ServiceFullDTO();
        serviceFullDTO.setDescription(servicePackName);
        serviceFullDTO.setPrice(price);
        serviceFullDTO.setTotalDuration(totalTime);

        List<String>packageName = selectedServices.stream().map(service -> service.get("label")).filter(Objects::nonNull).collect(Collectors.toList());

        ServiceEntity servicePack = modelMapper.map(serviceFullDTO, ServiceEntity.class);
        List<HairService> hairServices = packageName.stream()
                .map(hairServiceService::fromLabel)
                .collect(Collectors.toList());

        servicePack.setServices(hairServices);
        serviceRepository.save(servicePack);

        return "create-service";
    }




}
