package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.UserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;

public interface ClientService {
    void registerUser(UserRegisterDTO userRegisterDTO);
    public ClientEntity findOrCreateGuest(String username, String phone);

    ClientEntity findByPhone(String phone);

    void sendVerificationEmail(String email, String token);
}
