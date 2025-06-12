package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ClientRepositoryIT {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testGuardarYBuscarCliente() {
        ClientEntity client = new ClientEntity();
        client.setUsername("cliente2");
        client.setPassword("12345");
        client.setPhone("699999999");
        client.setGuest(false);
        client.setComments("VIP");
        client.setEnabled(true);

        client = clientRepository.save(client);

        Optional<ClientEntity> encontrado = clientRepository.findById(client.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getPhone()).isEqualTo("699999999");
    }
}