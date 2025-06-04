package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.HolidayRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Implementación de {@link SlotService} que gestiona la creación, búsqueda y eliminación de franjas horarias.
 */
@Service
public class SlotServiceImpl implements SlotService, CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SlotServiceImpl.class);

    private final HairdresserRepository hairdresserRepository;
    @Autowired
    private EntityManager entityManager;
    private final ServiceRepository serviceRepository;
    private final SlotRepository slotRepository;
    private final HolidayRepository holidayRepository;

    public SlotServiceImpl(HairdresserRepository hairdresserRepository,
                           ServiceRepository serviceRepository,
                           SlotRepository slotRepository,
                           HolidayRepository holidayRepository) {
        this.hairdresserRepository = hairdresserRepository;
        this.serviceRepository = serviceRepository;
        this.slotRepository = slotRepository;
        this.holidayRepository = holidayRepository;
    }
    // Logger, repositorios y dependencias inyectadas...

    /**
     * Carga automáticamente las franjas horarias al iniciar la aplicación.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadSlots();
    }

    /**
     * Genera franjas horarias para cada peluquero si no existen ya.
     */
    public void loadSlots() {
        // Obtener todos los peluqueros
        List<HairdresserEntity> hairdressers = hairdresserRepository.findAll();
        logger.info("Número de peluqueros encontrados: {}", hairdressers.size());

        // Definir el día de inicio (hoy, a las 10:00 AM)
        LocalDateTime startDate = LocalDateTime.now().withHour(Constants.START_HOUR).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = startDate.plusDays(Constants.DAYS_TOTAL_AVAILABLE); // 31 días en total

        // 🔴 Obtener días festivos definidos por el admin
        List<HolidayEntity> holidays = holidayRepository.findAll();
        Set<LocalDate> holidayDates = holidays.stream()
                .map(HolidayEntity::getDate)
                .collect(Collectors.toSet());

        List<SlotEntity> slots = new ArrayList<>();

        // Generar franjas para cada peluquero y cada día dentro del rango
        for (HairdresserEntity hairdresser : hairdressers) {

            for (LocalDateTime day = startDate; day.isBefore(endDate); day = day.plusDays(1)) {
                // ✅ Excluir domingos
                if (day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    logger.info("Día omitido (domingo): {}", day.toLocalDate());
                    continue;
                }
                // ✅ Excluir festivos definidos por el administrador
                if (holidayDates.contains(day.toLocalDate())) {
                    logger.info("Día omitido (festivo): {}", day.toLocalDate());
                    continue;
                }
                // Comprobar si ya existen franjas para este día
                List<SlotEntity> existingSlots = entityManager.createQuery(
                                "SELECT s FROM SlotEntity s WHERE s.hairdresser = :hairdresser AND s.startHour >= :startDate AND s.startHour < :endDate", SlotEntity.class)
                        .setParameter("hairdresser", hairdresser)
                        .setParameter("startDate", day.withMinute(0).withSecond(0))
                        .setParameter("endDate", day.plusDays(1))
                        .getResultList();

                if (existingSlots.isEmpty()) {
                    for (LocalDateTime timeSlot = day.withMinute(0).withSecond(0);
                         timeSlot.isBefore(day.plusHours(Constants.WORKED_HOURS));
                         timeSlot = timeSlot.plusMinutes(Constants.SLOT_INTERVAL)) {
                        // Crear la franja
                        SlotEntity slot = new SlotEntity();
                        slot.setHairdresser(hairdresser);
                        slot.setStartHour(timeSlot);
                        slot.setFinalHour(timeSlot.plusMinutes(Constants.SLOT_INTERVAL));
                        slot.setSlotStatus(SlotStatus.FREE); // Todas las franjas comienzan como libres

                        // Añadir a la lista
                        slots.add(slot);
                    }
                } else {
                    logger.info("Ya existen franjas para el día: {}", day);
                }
            }
        }

        // Persistir las franjas solo si no existen
        for (int i = 0; i < slots.size(); i++) {
            entityManager.persist(slots.get(i));
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // Flush final (solo una vez al final)
        entityManager.flush();
        entityManager.clear();


        // Programar tarea para borrar franjas del día actual
        scheduleSlotCleanup();
    }

    /**
     * Programa el borrado de franjas horarias al final del día actual.
     */
    public void scheduleSlotCleanup() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.withHour(23).withMinute(59).withSecond(0).withNano(0);

        // Si es un día distinto, el borrado debe ocurrir al final del día actual
        if (now.isBefore(midnight)) {
            long delay = ChronoUnit.MILLIS.between(now, midnight);

            // Usamos un temporizador para ejecutar el método después del delay
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Borrar franjas del día actual
                    deleteSlotsForToday();
                }
            }, delay);
        }
    }

    /**
     * Elimina las franjas correspondientes al día actual.
     */
    public void deleteSlotsForToday() {
        LocalDateTime today = LocalDateTime.now().withHour(Constants.START_HOUR).withMinute(0).withSecond(0).withNano(0);

        // Eliminar franjas para el día actual (solo las franjas del día que no han pasado)
        List<SlotEntity> slotsToDelete = entityManager.createQuery(
                        "SELECT s FROM SlotEntity s WHERE s.startHour >= :startDate AND s.startHour < :endDate", SlotEntity.class)
                .setParameter("startDate", today)
                .setParameter("endDate", today.plusDays(1))
                .getResultList();

        for (SlotEntity slot : slotsToDelete) {
            entityManager.remove(slot);
        }

        // Limpiar la cache
        entityManager.flush();
        entityManager.clear();


    }


    @Override
    public List<SlotEntity> getAvailableSlots(LocalDate localDate, Long packageId) {
        Optional<ServiceEntity> optionalPackage = serviceRepository.findById(packageId);
        if (optionalPackage.isEmpty()) {
            return Collections.emptyList();
        }
        ServiceEntity selectedPackage = optionalPackage.get();
        int requiredSlots = calculateTotalSlots(selectedPackage.getTotalDuration()); // e.g. 45min → 3 slots
        List<SlotStatus>slotPattern = selectedPackage.getPackageEnum().getSlotPattern();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        List<SlotEntity> availableSlots = slotRepository.findByStartHourBetween(startOfDay, endOfDay);

        return searchPattern(slotPattern, availableSlots);
    }


    @Override
    public List<List<SlotEntity>> getAvailableSlotCombinations(LocalDate localDate, Long packageId) {
        Optional<ServiceEntity> optionalPackage = serviceRepository.findById(packageId);
        if (optionalPackage.isEmpty()) {
            return Collections.emptyList();
        }

        ServiceEntity selectedPackage = optionalPackage.get();
        List<SlotStatus> slotPattern = selectedPackage.getPackageEnum().getSlotPattern();
        int totalSlots = slotPattern.size();


        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        List<SlotEntity> allSlots = slotRepository.findByStartHourBetween(startOfDay, endOfDay);

        // Agrupamos por hora
        Map<LocalDateTime, List<SlotEntity>> slotsByTime = allSlots.stream()
                .collect(Collectors.groupingBy(SlotEntity::getStartHour));

        List<List<SlotEntity>> validCombinations = new ArrayList<>();

        for (LocalDateTime potentialStart = startOfDay;
             !potentialStart.plusMinutes(totalSlots * 15L).isAfter(endOfDay);
             potentialStart = potentialStart.plusMinutes(15)) {

            // Llamamos al generador recursivo
            generateCombinations(
                    potentialStart,
                    slotPattern,
                    slotsByTime,
                    new ArrayList<>(),
                    0,
                    validCombinations
            );
        }

        return validCombinations;
    }

    /**
     * Algoritmo recursivo para generar combinaciones de franjas válidas según un patrón.
     */
    private void generateCombinations(
            LocalDateTime currentTime,
            List<SlotStatus> pattern,
            Map<LocalDateTime, List<SlotEntity>> slotsByTime,
            List<SlotEntity> currentCombination,
            int patternIndex,
            List<List<SlotEntity>> allCombinations
    ) {
        if (patternIndex == pattern.size()) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        LocalDateTime slotTime = currentTime.plusMinutes(patternIndex * Constants.SLOT_INTERVAL);
        List<SlotEntity> options = slotsByTime.getOrDefault(slotTime, List.of());

        for (SlotEntity slot : options) {
            // Verificar continuidad temporal
            if (!currentCombination.isEmpty()) {
                SlotEntity previousSlot = currentCombination.getLast();
                if (!previousSlot.getFinalHour().equals(slot.getStartHour())) {
                    continue;
                }
            }

            // Verificar coincidencia de estado o permitir flexibilidad
            SlotStatus expectedStatus = pattern.get(patternIndex);
            if (slot.getSlotStatus() == expectedStatus || (expectedStatus == SlotStatus.OCCUPIED && slot.getSlotStatus() == SlotStatus.FREE)) {
                currentCombination.add(slot);
                generateCombinations(currentTime, pattern, slotsByTime, currentCombination, patternIndex + 1, allCombinations);
                currentCombination.removeLast();
            }
        }
    }


    /**
     * Busca franjas que coincidan con un patrón de estados (FREE, OCCUPIED, etc.).
     */
    public List<SlotEntity> searchPattern(List<SlotStatus> slotPattern, List<SlotEntity> availableSlots) {
        List<SlotEntity> validSlots = new ArrayList<>();
        int patternIndex = 0;  // Índice del patrón actual

        for (int i = 0; i < availableSlots.size(); i++) {
            SlotEntity currentSlot = availableSlots.get(i);
            SlotStatus currentStatus = currentSlot.getSlotStatus();
            SlotStatus expectedStatus = slotPattern.get(patternIndex);

            // Comprobamos si el patrón y el estado del slot son compatibles
            if (expectedStatus == SlotStatus.FREE) {
                // Si el patrón es free, solo aceptamos free en esa posición
                if (currentStatus == SlotStatus.FREE) {
                    validSlots.add(currentSlot);
                    patternIndex++;
                }
            } else if (expectedStatus == SlotStatus.OCCUPIED) {
                // Si el patrón es occupied, aceptamos tanto occupied como free
                validSlots.add(currentSlot);
                patternIndex++;
            }

            // Si hemos completado un ciclo del patrón, lo reiniciamos para continuar buscando
            if (patternIndex == slotPattern.size()) {
                patternIndex = 0;
            }
        }

        return validSlots;
    }

    @Override
    public int calculateTotalSlots(int totalDuration) {
        return totalDuration/Constants.SLOT_INTERVAL;
    }

    @Override
    public List<List<SlotEntity>> getSlotCombinationsForDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<SlotEntity> allSlotsForDate = slotRepository.findAllByStartHourDateOrderByStartHourAsc(date);

        List<List<SlotEntity>> combinations = new ArrayList<>();
        List<SlotEntity> currentGroup = new ArrayList<>();

        for (SlotEntity slot : allSlotsForDate) {
            if (slot.getSlotStatus() != SlotStatus.FREE) continue;

            if (currentGroup.isEmpty()) {
                currentGroup.add(slot);
            } else {
                SlotEntity last = currentGroup.getLast();

                // Verifica continuidad horaria y peluquero
                if (slot.getHairdresser().equals(last.getHairdresser())
                        && slot.getStartHour().equals(last.getFinalHour())) {
                    currentGroup.add(slot);
                } else {
                    combinations.add(new ArrayList<>(currentGroup));
                    currentGroup.clear();
                    currentGroup.add(slot);
                }
            }
        }

        // Agrega el último grupo si quedó algo
        if (!currentGroup.isEmpty()) {
            combinations.add(currentGroup);
        }

        return combinations;
    }

    @Override
    public List<SlotEntity> getFirstAvailableSlotCombinationForHour(LocalDate localDate, Long packageId, String selectedHourRange) {
        // Convertir la hora seleccionada (e.g. "10:00 - 10:45") en la hora de inicio
        String[] hourRangeParts = selectedHourRange.split(" - ");
        String startHour = hourRangeParts[0].trim();  // Ejemplo: "10:00"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime selectedStartTime = LocalTime.parse(startHour, formatter);

        // Obtener las combinaciones de franjas disponibles
        List<List<SlotEntity>> availableSlotCombinations = getAvailableSlotCombinations(localDate, packageId);

        // Buscar la primera combinación de franjas que empiece con la hora seleccionada
        for (List<SlotEntity> combination : availableSlotCombinations) {
            // Verificar que la primera franja de la combinación comience con la hora seleccionada
            SlotEntity firstSlot = combination.getFirst();

            // Extraer la hora de la franja (asumiendo que getStartHour devuelve un LocalDateTime)
            LocalDateTime firstSlotStartHour = firstSlot.getStartHour();
            LocalTime firstSlotStart = firstSlotStartHour.toLocalTime();  // Convertir a LocalTime

            // Si la hora de inicio de la primera franja coincide con la hora seleccionada
            if (firstSlotStart.equals(selectedStartTime)) {
                return combination;  // Retornar la combinación encontrada
            }
        }

        return Collections.emptyList();  // Si no se encuentra ninguna coincidencia
    }

    @Override
    public Map<LocalDate, List<SlotEntity>> slotOrdering(HairdresserEntity hairdresser) {
        return hairdresser.getSlots().stream()
                .sorted(Comparator.comparing(SlotEntity::getStartHour))
                .collect(Collectors.groupingBy(
                        slot -> slot.getStartHour().toLocalDate(),
                        LinkedHashMap::new, // mantiene orden de inserción
                        Collectors.toList()
                ));
    }
    @Override
    public List<SlotStatus>convertBlockTypesToSlotStatuses(List<Map<String, String>> blocks){
        List<SlotStatus>slotStatusList = new ArrayList<>();
        for(Map<String, String> block : blocks){
            String type = block.get("type");
            SlotStatus status;
            if(Constants.ACTIVE.equalsIgnoreCase(type)){
                status = SlotStatus.FREE;
            }else if(Constants.PASSIVE.equalsIgnoreCase(type)){
                status = SlotStatus.OCCUPIED;
            }else{
                throw new IllegalArgumentException("Typo desconocido: " + type);
            }
            slotStatusList.add(status);
        }
        return slotStatusList;
    }

    @Override
    @Transactional
    public void deleteSlotsInHolidays() {
        List<HolidayEntity> holidays = holidayRepository.findAll();

        for (HolidayEntity holiday : holidays) {
            LocalDateTime startOfDay = holiday.getDate().atTime(Constants.START_HOUR, 0);
            LocalDateTime endOfDay = startOfDay.plusHours(Constants.WORKED_HOURS);

            List<SlotEntity> slotsToDelete = slotRepository.findByStartHourBetween(startOfDay, endOfDay);

            slotRepository.deleteAll(slotsToDelete);
        }
    }


}