package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentFilter;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.OpeningHours;
import de.hda.fbi.efussgaengerzone.domain.model.shop.WeeklyOpeningHours;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AppointmentSchedulingTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentScheduling appointmentScheduling;

    private UUID shopId;
    private Shop shop;
    private Appointment appointment;
    private AppointmentFilter filter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        shopId = UUID.randomUUID();

        OpeningHours mondayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours tuesdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours wednesdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours thursdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours fridayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours saturdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(14, 0));

        WeeklyOpeningHours weeklyOpeningHours = new WeeklyOpeningHours(
                mondayHours,
                tuesdayHours,
                wednesdayHours,
                thursdayHours,
                fridayHours,
                saturdayHours
        );

        shop = new Shop(shopId, "Test Shop", "Description", new HashSet<>(), weeklyOpeningHours, new HashSet<>(), true, 30, "owner@example.com");

        appointment = new Appointment(UUID.randomUUID(), "John Doe", null, "123456789", LocalDateTime.now(), null);

        // Simple filter that accepts all appointments
        filter = appointment -> true;
    }

    @Test
    public void testSearchAppointments() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of(appointment));

        Collection<Appointment> results = appointmentScheduling.searchAppointments(shopId, Set.of(filter));

        assertEquals(1, results.size());
        assertEquals(appointment, results.iterator().next());
    }

    @Test
    public void testSearchAppointments_ShopNotFound() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        Collection<Appointment> results = appointmentScheduling.searchAppointments(shopId, Set.of(filter));

        assertEquals(0, results.size());
    }
}