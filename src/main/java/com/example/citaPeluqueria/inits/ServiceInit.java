package com.example.citaPeluqueria.inits;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
/**
 * Componente que inicializa los servicios de peluquería en la base de datos al arrancar la aplicación,
 * si aún no existen registros.
 *
 * <p>Este inicializador recorre todos los valores definidos en {@link ServicePackageEnum} y
 * crea entidades {@link ServiceEntity} con datos asociados como precio, duración y lista de servicios.
 * La lógica de precios, duración y servicios asociados está centralizada en métodos privados.
 */
@Component
public class ServiceInit implements CommandLineRunner {

    private final ServiceRepository serviceRepository;

    public ServiceInit(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

     /**
     * Ejecuta la lógica de inicialización si la base de datos no contiene ya servicios registrados.
     */
    @Override
    public void run(String... args) {
        if (serviceRepository.count() > 0) {
            return;
        }

        List<ServiceEntity> services = Arrays.stream(ServicePackageEnum.values())
                .map(pkg -> {
                    ServiceEntity service = new ServiceEntity();
                    service.setPackageEnum(pkg);
                    service.setPrice(getPriceFor(pkg));
                    service.setTotalDuration(getDurationFor(pkg));
                    service.setDescription(pkg.getDisplayName()); // Spanish name for UI
                    service.setServices(getHairServicesFor(pkg));
                    return service;
                })
                .toList();

        serviceRepository.saveAll(services);
    }

    /**
     * Devuelve el precio correspondiente al paquete de servicio.
     *
     * @param pkg el paquete de servicio
     * @return precio en euros
     */
    private double getPriceFor(ServicePackageEnum pkg) {
        return switch (pkg) {
            case CUT_WASH_BLOWDRY -> 25;
            case COLOR_WASH_BLOWDRY -> 35;
            case HIGHLIGHTS_CUT_WASH -> 45;
            case HIGHLIGHTS_CUT_WASH_BLOWDRY -> 55;
            case CURL_BLOWDRY -> 30;
            case COLOR_HIGHLIGHTS_WASH_BLOWDRY -> 65;
            case CUT_MASK -> 28;
            case COLOR_MASK -> 38;
            case FULL_TREATMENT -> 70;
            case STRAIGHTEN_BLOWDRY -> 32;
            case CUT_HIGHLIGHTS -> 40;
            case CURL_HIGHLIGHTS -> 42;
            case STRAIGHTEN_HIGHLIGHTS -> 43;
            case HYDRATION_BLOWDRY -> 34;
        };
    }
    /**
     * Devuelve la duración total en minutos del paquete de servicio.
     *
     * @param pkg el paquete de servicio
     * @return duración en minutos
     */
    private int getDurationFor(ServicePackageEnum pkg) {
        return switch (pkg) {
            case CUT_WASH_BLOWDRY -> 45;
            case COLOR_WASH_BLOWDRY -> 60;
            case HIGHLIGHTS_CUT_WASH -> 75;
            case HIGHLIGHTS_CUT_WASH_BLOWDRY -> 90;
            case CURL_BLOWDRY -> 50;
            case COLOR_HIGHLIGHTS_WASH_BLOWDRY -> 95;
            case CUT_MASK -> 50;
            case COLOR_MASK -> 60;
            case FULL_TREATMENT -> 100;
            case STRAIGHTEN_BLOWDRY -> 55;
            case CUT_HIGHLIGHTS -> 70;
            case CURL_HIGHLIGHTS -> 75;
            case STRAIGHTEN_HIGHLIGHTS -> 75;
            case HYDRATION_BLOWDRY -> 45;
        };
    }
    /**
     * Devuelve la lista de servicios individuales ({@link HairService}) que componen un paquete.
     *
     * @param pkg el paquete de servicio
     * @return lista de servicios
     */
    private List<HairService> getHairServicesFor(ServicePackageEnum pkg) {
        return switch (pkg) {
            case CUT_WASH_BLOWDRY -> List.of(HairService.CUT, HairService.WASH, HairService.BLOWDRY);
            case COLOR_WASH_BLOWDRY -> List.of(HairService.COLOR, HairService.WASH, HairService.BLOWDRY);
            case HIGHLIGHTS_CUT_WASH -> List.of(HairService.HIGHLIGHTS, HairService.CUT, HairService.WASH);
            case HIGHLIGHTS_CUT_WASH_BLOWDRY -> List.of(HairService.HIGHLIGHTS, HairService.CUT, HairService.WASH, HairService.BLOWDRY);
            case CURL_BLOWDRY -> List.of(HairService.CURL, HairService.BLOWDRY);
            case COLOR_HIGHLIGHTS_WASH_BLOWDRY -> List.of(HairService.COLOR, HairService.HIGHLIGHTS, HairService.WASH, HairService.BLOWDRY);
            case CUT_MASK -> List.of(HairService.CUT, HairService.MASK);
            case COLOR_MASK -> List.of(HairService.COLOR, HairService.MASK);
            case FULL_TREATMENT -> List.of(HairService.CUT, HairService.COLOR, HairService.HIGHLIGHTS, HairService.WASH, HairService.MASK, HairService.BLOWDRY, HairService.STRAIGHTEN, HairService.CURL);
            case STRAIGHTEN_BLOWDRY -> List.of(HairService.STRAIGHTEN, HairService.BLOWDRY);
            case CUT_HIGHLIGHTS -> List.of(HairService.CUT, HairService.HIGHLIGHTS);
            case CURL_HIGHLIGHTS -> List.of(HairService.CURL, HairService.HIGHLIGHTS);
            case STRAIGHTEN_HIGHLIGHTS -> List.of(HairService.STRAIGHTEN, HairService.HIGHLIGHTS);
            case HYDRATION_BLOWDRY -> List.of(HairService.HYDRATION, HairService.BLOWDRY);
        };
    }
}
