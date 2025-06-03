package com.example.citaPeluqueria.scheduling;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SchedulerTasks {

    private final AppointmentRepository appointmentRepository;

    public SchedulerTasks(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

 /*   @Scheduled(cron = "0 1 0 * * ?")
    public void generateNext30Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = startDate.plusDays(30);

        List<AvailableDayEntity> existing = availableDayRepository.findByDateBetween(startDate, endDate);
        if (existing.isEmpty()) {
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                AvailableDayEntity day = new AvailableDayEntity();
                day.setDate(date);
                day.setAvailable(true);
                availableDayRepository.save(day);
            }
        }
    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void archiveYesterdayAppointments() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<AppointmentEntity> oldAppointments = appointmentRepository.findByDate(yesterday);

        for (AppointmentEntity a : oldAppointments) {
            ArchivedAppointmentEntity archived = new ArchivedAppointmentEntity();
            archived.setDate(a.getDate());
            archived.setSelectedHourRange(a.getSelectedHourRange());
            archived.setService(a.getService());
            archived.setUser(a.getUser());
            archivedAppointmentRepository.save(archived);
            appointmentRepository.delete(a);
        }
    }*/
}


