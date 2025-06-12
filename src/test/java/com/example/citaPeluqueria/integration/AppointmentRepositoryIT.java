package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppointmentRepositoryIT {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void testSaveAndFindById() {
        ClientEntity client = new ClientEntity();
        client.setEmail("juan@mail.com");
        client.setPhone("111222333");
        client.setGuest(false);
        client.setComments("sin comentarios");
        client.setSlots(Set.of());
        client.setAppointments(Set.of());
        client.setEnabled(true);
        client = clientRepository.save(client);

        ServiceEntity service = new ServiceEntity();
        service.setPackageEnum(ServicePackageEnum.COLOR_MASK);
        service.setPrice(20.0);
        service.setTotalDuration(30);
        service.setDescription("Corte");
        service.isActive();
        service.setServices(List.of(HairService.CUT));
        service.setSlots(Set.of());
        service.setAppointments(Set.of());
        service = serviceRepository.save(service);

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(client);
        appointment.setService(service);
        appointment.setDate("2025-06-14");
        appointment.setSelectedHourRange("10:00-10:30");

        AppointmentEntity saved = appointmentRepository.save(appointment);

        Optional<AppointmentEntity> found = appointmentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getPhone()).isEqualTo("111222333");
    }
}