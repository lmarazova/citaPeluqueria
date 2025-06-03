package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.entities.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

public interface AppointmentService {

    public List<AppointmentEntity> getAppointmentsByUser(ClientEntity user);

    void prepareHourSelectionView(Model model, Long packageId, String date, ClientEntity user);


    boolean createAppointment(String selectedHourRange, Long packageId, String date, ClientEntity user);

    void deleteClientAppointment(Long appointmentId, Principal principal);

    void deleteByClientDateAndHourRange(Long userId, String date, String selectedHourRange);
}
