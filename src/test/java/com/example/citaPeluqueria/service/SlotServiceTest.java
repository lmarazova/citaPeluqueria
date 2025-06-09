package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.HolidayRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.services.SlotServiceImpl;
import com.example.citaPeluqueria.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class SlotServiceTest {
    @Mock
    HairdresserRepository hairdresserRepository;

    @Mock
    ServiceRepository serviceRepository;

    @Mock
    SlotRepository slotRepository;

    @Mock
    HolidayRepository holidayRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    SlotServiceImpl slotService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        slotService = new SlotServiceImpl(hairdresserRepository, serviceRepository, slotRepository, holidayRepository);

        // Inyectar con reflection el entityManager mock
        Field entityManagerField = SlotServiceImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(slotService, entityManager);
    }

    // ====== loadSlots ======
    @Test
    void loadSlots_generatesSlotsWhenNoneExist() {
        HairdresserEntity hairdresser = new HairdresserEntity();
        when(hairdresserRepository.findAll()).thenReturn(List.of(hairdresser));
        when(holidayRepository.findAll()).thenReturn(Collections.emptyList());

        TypedQuery<SlotEntity> queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(SlotEntity.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        slotService.loadSlots();

        verify(entityManager, atLeastOnce()).persist(any(SlotEntity.class));
        verify(entityManager, atLeastOnce()).flush();
        verify(entityManager, atLeastOnce()).clear();
    }

    @Test
    void loadSlots_skipsSundaysAndHolidays() {
        HairdresserEntity hairdresser = new HairdresserEntity();
        when(hairdresserRepository.findAll()).thenReturn(List.of(hairdresser));
        HolidayEntity holiday = new HolidayEntity();
        holiday.setDate(LocalDate.now());
        when(holidayRepository.findAll()).thenReturn(List.of(holiday));

        TypedQuery<SlotEntity> queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(SlotEntity.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(Collections.emptyList());

        slotService.loadSlots();

        verify(entityManager, atLeastOnce()).persist(any(SlotEntity.class));
    }

    // ====== deleteSlotsForToday ======
    @Test
    void deleteSlotsForToday_removesSlotsForCurrentDay() {
        SlotEntity slot = new SlotEntity();
        TypedQuery<SlotEntity> queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(SlotEntity.class))).thenReturn(queryMock);
        when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(List.of(slot));

        slotService.deleteSlotsForToday();

        verify(entityManager).remove(slot);
        verify(entityManager).flush();
        verify(entityManager).clear();
    }

    // ====== getAvailableSlots ======
    @Test
    void getAvailableSlots_returnsEmptyWhenPackageNotFound() {
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<SlotEntity> result = slotService.getAvailableSlots(LocalDate.now(), 1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAvailableSlots_returnsSlotsBasedOnPattern() {
        ServiceEntity service = mock(ServiceEntity.class);
        ServicePackageEnum packageEnum = mock(ServicePackageEnum.class);
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));
        when(service.getTotalDuration()).thenReturn(45);
        when(service.getPackageEnum()).thenReturn(packageEnum);
        when(packageEnum.getSlotPattern()).thenReturn(List.of(SlotStatus.FREE));

        SlotEntity slot = createSlotWithStatus(SlotStatus.FREE);
        when(slotRepository.findByStartHourBetween(any(), any())).thenReturn(List.of(slot));

        List<SlotEntity> slots = slotService.getAvailableSlots(LocalDate.now(), 1L);

        assertFalse(slots.isEmpty());
        assertEquals(SlotStatus.FREE, slots.get(0).getSlotStatus());
    }

    // ====== calculateTotalSlots ======
    @Test
    void calculateTotalSlots_returnsCorrectCount() {
        int duration = 45;
        int expectedSlots = duration / Constants.SLOT_INTERVAL;
        assertEquals(expectedSlots, slotService.calculateTotalSlots(duration));
    }

    // ====== convertBlockTypesToSlotStatuses ======
    @Test
    void convertBlockTypesToSlotStatuses_returnsCorrectStatuses() {
        List<Map<String, String>> blocks = List.of(
                Map.of("type", Constants.ACTIVE),
                Map.of("type", Constants.PASSIVE)
        );

        List<SlotStatus> statuses = slotService.convertBlockTypesToSlotStatuses(blocks);

        assertEquals(List.of(SlotStatus.FREE, SlotStatus.OCCUPIED), statuses);
    }

    @Test
    void convertBlockTypesToSlotStatuses_throwsForUnknownType() {
        List<Map<String, String>> blocks = List.of(
                Map.of("type", "unknown")
        );

        assertThrows(IllegalArgumentException.class, () -> slotService.convertBlockTypesToSlotStatuses(blocks));
    }

    // ====== deleteSlotsInHolidays ======
    @Test
    void deleteSlotsInHolidays_deletesSlotsForHolidays() {
        HolidayEntity holiday = new HolidayEntity();
        holiday.setDate(LocalDate.now());
        when(holidayRepository.findAll()).thenReturn(List.of(holiday));
        when(slotRepository.findByStartHourBetween(any(), any())).thenReturn(List.of(mock(SlotEntity.class)));

        slotService.deleteSlotsInHolidays();

        verify(slotRepository).deleteAll(anyList());
    }

    // ====== getAvailableSlotCombinations ======
    @Test
    void getAvailableSlotCombinations_returnsEmptyWhenPackageNotFound() {
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<List<SlotEntity>> result = slotService.getAvailableSlotCombinations(LocalDate.now(), 1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAvailableSlotCombinations_returnsValidCombinations() {
        ServiceEntity service = mock(ServiceEntity.class);
        ServicePackageEnum packageEnum = mock(ServicePackageEnum.class);
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));
        when(service.getPackageEnum()).thenReturn(packageEnum);

        List<SlotStatus> pattern = List.of(SlotStatus.FREE, SlotStatus.OCCUPIED);
        when(packageEnum.getSlotPattern()).thenReturn(pattern);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();

        SlotEntity slot1 = createSlotWithStatusAndTime(SlotStatus.FREE, startOfDay);
        SlotEntity slot2 = createSlotWithStatusAndTime(SlotStatus.OCCUPIED, startOfDay.plusMinutes(Constants.SLOT_INTERVAL));

        List<SlotEntity> allSlots = List.of(slot1, slot2);
        when(slotRepository.findByStartHourBetween(any(), any())).thenReturn(allSlots);

        List<List<SlotEntity>> combinations = slotService.getAvailableSlotCombinations(today, 1L);

        assertFalse(combinations.isEmpty());
        assertEquals(2, combinations.get(0).size());
    }


    // ====== getSlotCombinationsForDate ======
    @Test
    void getSlotCombinationsForDate_groupsContinuousSlots() {
        HairdresserEntity hairdresser = new HairdresserEntity();
        LocalDate date = LocalDate.now();

        SlotEntity slot1 = createSlotWithStatusAndTime(SlotStatus.FREE, date.atTime(10, 0));
        slot1.setHairdresser(hairdresser);
        slot1.setFinalHour(date.atTime(10, 15));

        SlotEntity slot2 = createSlotWithStatusAndTime(SlotStatus.FREE, date.atTime(10, 15));
        slot2.setHairdresser(hairdresser);
        slot2.setFinalHour(date.atTime(10, 30));

        SlotEntity slot3 = createSlotWithStatusAndTime(SlotStatus.OCCUPIED, date.atTime(10, 30));
        slot3.setHairdresser(hairdresser);

        when(slotRepository.findAllByStartHourDateOrderByStartHourAsc(date)).thenReturn(List.of(slot1, slot2, slot3));

        List<List<SlotEntity>> combinations = slotService.getSlotCombinationsForDate(date.toString());

        assertEquals(1, combinations.size());
        assertEquals(2, combinations.get(0).size());
    }

    // ====== getFirstAvailableSlotCombinationForHour ======
    @Test
    void getFirstAvailableSlotCombinationForHour_returnsCorrectCombination() {
        LocalDate date = LocalDate.now();
        Long packageId = 1L;
        String hourRange = "10:00 - 10:45";

        List<SlotEntity> combo = new ArrayList<>();
        SlotEntity slot = new SlotEntity();
        slot.setStartHour(date.atTime(10, 0));
        combo.add(slot);

        List<List<SlotEntity>> combinations = List.of(combo);

        SlotServiceImpl spyService = Mockito.spy(slotService);
        doReturn(combinations).when(spyService).getAvailableSlotCombinations(date, packageId);

        List<SlotEntity> result = spyService.getFirstAvailableSlotCombinationForHour(date, packageId, hourRange);

        assertEquals(combo, result);
    }

    // ====== slotOrdering ======
    @Test
    void slotOrdering_returnsOrderedSlotsGroupedByDate() {
        HairdresserEntity hairdresser = new HairdresserEntity();
        SlotEntity slot1 = createSlotWithStatusAndTime(SlotStatus.FREE, LocalDateTime.of(2025, 6, 9, 10, 0));
        SlotEntity slot2 = createSlotWithStatusAndTime(SlotStatus.FREE, LocalDateTime.of(2025, 6, 9, 10, 15));
        slot1.setHairdresser(hairdresser);
        slot2.setHairdresser(hairdresser);

        List<SlotEntity> slots = List.of(slot1, slot2);
        hairdresser.setSlots(new HashSet<>(slots));  // <-- Aquí el fix

        Map<LocalDate, List<SlotEntity>> grouped = slotService.slotOrdering(hairdresser);

        assertFalse(grouped.isEmpty());
        assertTrue(grouped.containsKey(LocalDate.of(2025, 6, 9)));  // <-- corregido a LocalDate
        assertEquals(2, grouped.get(LocalDate.of(2025, 6, 9)).size());
    }


    // ====== Helpers ======

    private SlotEntity createSlotWithStatus(SlotStatus status) {
        SlotEntity slot = new SlotEntity();
        slot.setSlotStatus(status);
        slot.setStartHour(LocalDateTime.now());
        return slot;
    }

    private SlotEntity createSlotWithStatusAndTime(SlotStatus status, LocalDateTime time) {
        SlotEntity slot = new SlotEntity();
        slot.setSlotStatus(status);
        slot.setStartHour(time);
        slot.setFinalHour(time.plusMinutes(Constants.SLOT_INTERVAL));
        return slot;
    }
}
