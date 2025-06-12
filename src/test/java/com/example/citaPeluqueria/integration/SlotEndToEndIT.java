package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SlotEndToEndIT {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private HairdresserRepository hairdresserRepository;

    @Test
    void crearSlotConPeluqueroYClienteYConsultar() {
        // Crear cliente
        ClientEntity client = new ClientEntity();
        client.setUsername("laura");
        client.setPassword("clave123");
        client.setPhone("611111111");
        client.setEnabled(true);
        client = clientRepository.save(client);

        // Crear peluquero
        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setUsername("Pepe Peluquero");
        hairdresser = hairdresserRepository.save(hairdresser);

        // Crear slot
        SlotEntity slot = new SlotEntity();
        slot.setStartHour(LocalDateTime.of(2025, 6, 12, 11, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 12, 11, 30));
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slot.setClient(client);
        slot.setHairdresser(hairdresser);
        slot = slotRepository.save(slot);

        // Verificar
        Optional<SlotEntity> loaded = slotRepository.findById(slot.getId());
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getHairdresser().getUsername()).isEqualTo("Pepe Peluquero");
        assertThat(loaded.get().getClient().getUsername()).isEqualTo("laura");
    }
}
