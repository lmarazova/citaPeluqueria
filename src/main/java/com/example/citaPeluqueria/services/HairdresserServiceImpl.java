package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.HairdresserRegisterDTO;
import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
/**
 * Implementación del servicio {@link HairdresserService} que permite
 * registrar peluqueros en el sistema.
 */
@Service
public class HairdresserServiceImpl implements HairdresserService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private HairdresserRepository hairdresserRepository;



    /**
     * Registra un nuevo peluquero convirtiendo el DTO a entidad, cifrando su contraseña
     * y asignando el rol {@link Role#MODERATOR}.
     *
     * @param hairdresserRegisterDTO objeto con los datos necesarios para crear un peluquero.
     */
    @Override
    public void registerHairdresser(HairdresserRegisterDTO hairdresserRegisterDTO) {
        HairdresserEntity hairdresser = modelMapper.map(hairdresserRegisterDTO, HairdresserEntity.class);
        hairdresser.setPassword(passwordEncoder.encode(hairdresserRegisterDTO.getPassword()));
        hairdresser.setRoles(Set.of(Role.MODERATOR));
        hairdresserRepository.save(hairdresser);
    }
}
