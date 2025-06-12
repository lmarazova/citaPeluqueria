package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppointmentEndToEndIT {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void crearClienteYCitaYConsultar() {
        // Crear cliente
        ClientEntity client = new ClientEntity();
        client.setUsername("juanito");
        client.setPassword("passt");
        client.setPhone("600000000");
        client.setGuest(false);
        client.setEnabled(true);
        client = clientRepository.save(client);

        // Crear servicio
        ServiceEntity service = new ServiceEntity();
        service.setPackageEnum(ServicePackageEnum.CUT_WASH_BLOWDRY);
        service.setPrice(20.0);
        service.setTotalDuration(30);
        service.setDescription("Corte rápido");
        service.setServices(List.of(HairService.CUT));
        service = serviceRepository.save(service);

        // Crear cita
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(client);
        appointment.setService(service);
        appointment.setDate("2025-06-12");
        appointment.setSelectedHourRange("10:00-10:30");
        appointment = appointmentRepository.save(appointment);

        // Verificar
        Optional<AppointmentEntity> loaded = appointmentRepository.findById(appointment.getId());
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getUser().getPhone()).isEqualTo("600000000");
        assertThat(loaded.get().getService().getDescription()).isEqualTo("Corte rápido");
    }
}