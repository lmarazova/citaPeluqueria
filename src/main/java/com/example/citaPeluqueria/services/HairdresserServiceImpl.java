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

@Service
public class HairdresserServiceImpl implements HairdresserService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private HairdresserRepository hairdresserRepository;




    @Override
    public void registerHairdresser(HairdresserRegisterDTO hairdresserRegisterDTO) {
        HairdresserEntity hairdresser = modelMapper.map(hairdresserRegisterDTO, HairdresserEntity.class);
        hairdresser.setPassword(passwordEncoder.encode(hairdresserRegisterDTO.getPassword()));
        hairdresser.setRoles(Set.of(Role.MODERATOR));
        hairdresserRepository.save(hairdresser);
    }
}
