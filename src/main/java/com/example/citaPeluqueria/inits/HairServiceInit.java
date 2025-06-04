package com.example.citaPeluqueria.inits;

import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
/**
 * Clase de inicialización que se ejecuta al arrancar la aplicación.
 * Inserta en la base de datos todos los valores posibles del enum {@link HairService},
 * creando las entidades correspondientes si aún no existen.
 *
 * Esta clase implementa {@link CommandLineRunner}, por lo que su método {@code run()}
 * se ejecuta automáticamente al inicio de la aplicación.
 */
@Component
public class HairServiceInit implements CommandLineRunner {

    private final HairServiceRepository hairServiceRepository;

    /**
     * Constructor con inyección de dependencias para acceder al repositorio.
     *
     * @param hairServiceRepository Repositorio que gestiona entidades de tipo {@link HairServiceEntity}
     */
    public HairServiceInit(HairServiceRepository hairServiceRepository) {
        this.hairServiceRepository = hairServiceRepository;
    }


    /**
     * Método que se ejecuta al iniciar la aplicación. Comprueba si la tabla de servicios capilares
     * ya contiene datos. Si está vacía, inserta una entidad por cada valor definido en el enum {@link HairService}.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    @Override
    public void run(String... args) throws Exception {
        if (hairServiceRepository.count() > 0) {
            return;
        }
        List<HairServiceEntity> hairServices = Arrays.stream(HairService.values())
                .map(service -> {
                    HairServiceEntity hairService = new HairServiceEntity();
                    hairService.setHairService(service);
                    return hairService;
                })
                .toList();

        hairServiceRepository.saveAll(hairServices);
    }
}
