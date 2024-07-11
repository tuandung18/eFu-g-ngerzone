package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentFilter;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentFilterFuture;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.OpeningHours;
import de.hda.fbi.efussgaengerzone.domain.model.shop.WeeklyOpeningHours;
import java.time.DayOfWeek;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppointmentSchedulingTest {

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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shopId = UUID.randomUUID();

        OpeningHours mondayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours tuesdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours wednesdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours thursdayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
        OpeningHours fridayHours = new OpeningHours(LocalTime.of(9, 0), LocalTime.of(17, 0));

        WeeklyOpeningHours weeklyOpeningHours = new WeeklyOpeningHours(
                mondayHours,
                tuesdayHours,
                wednesdayHours,
                thursdayHours,
                fridayHours,
                null
        );

        shop = new Shop(shopId, "Test Shop", "Description", new HashSet<>(), weeklyOpeningHours, new HashSet<>(), true, 30, "owner@example.com");

        appointment = new Appointment(UUID.randomUUID(), "John Doe", null, "123456789", LocalDateTime.now().plusDays(3), Duration.ofMinutes(15));

        // Simple filter that accepts all appointments
        filter = appointment -> true;
    }

    @Test
    void testSearchAppointments() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of(appointment));

        Collection<Appointment> results = appointmentScheduling.searchAppointments(shopId, Set.of(filter));

        assertEquals(1, results.size());
        assertEquals(appointment, results.iterator().next());
    }

    @Test
    void testSearchAppointments_ShopNotFound() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        Collection<Appointment> results = appointmentScheduling.searchAppointments(shopId, Set.of(filter));

        assertEquals(0, results.size());
    }

    @Test
    void whenNextAppointmentExists() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of(appointment));
        when(appointmentScheduling.searchAppointments(shopId, Set.of(AppointmentFilterFuture.INSTANCE)))
                .thenReturn(List.of(appointment));

        Optional<Appointment> nextAppointment = appointmentScheduling.findNextAppointment(shopId);

        Assertions.assertThat(nextAppointment).isPresent();
        assertEquals(appointment, nextAppointment.get());
    }

    @Test
    void whenNextAppointmentDoesntExist() {
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
        when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of());

        Optional<Appointment> nextAppointment = appointmentScheduling.findNextAppointment(shopId);

        Assertions.assertThat(nextAppointment).isEmpty();
    }

    @Nested
    class AvailableDatesOnDayTests {

        @Test
        void returnsEmptyListWhenShopIsClosedOnDay() {
            // given
            when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

            // when
            List<LocalTime> availableTimes = appointmentScheduling.availableDatesOnDay(shopId, DayOfWeek.SATURDAY);

            // then
            assertEquals(0, availableTimes.size());
        }

        @Test
        void returnsAllTimesWhenNoAppointmentsBooked() {
            // given
            when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));
            when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of());

            // when
            List<LocalTime> availableTimes = appointmentScheduling.availableDatesOnDay(shopId, DayOfWeek.MONDAY);

            // then
            List<LocalTime> expectedTimes = List.of(
                    LocalTime.of(9, 0),
                    LocalTime.of(9, 30),
                    LocalTime.of(10, 0),
                    LocalTime.of(10, 30),
                    LocalTime.of(11, 0),
                    LocalTime.of(11, 30),
                    LocalTime.of(12, 0),
                    LocalTime.of(12, 30),
                    LocalTime.of(13, 0),
                    LocalTime.of(13, 30),
                    LocalTime.of(14, 0),
                    LocalTime.of(14, 30),
                    LocalTime.of(15, 0),
                    LocalTime.of(15, 30),
                    LocalTime.of(16, 0),
                    LocalTime.of(16, 30)
            );

            assertEquals(expectedTimes, availableTimes);
        }

        @Test
        void returnsAvailableTimesWhenSomeAppointmentsBooked() {
            // given
            when(shopRepository.findById(shopId)).thenReturn(Optional.of(shop));

            /* Calculate the next Monday from the current date.
               This is important to accurately test the appointment scheduling functionality,
               ensuring that the system can handle future appointments correctly. */
            LocalDateTime nextMonday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(9).withMinute(0).withSecond(0).withNano(0);

            Appointment bookedAppointment = new Appointment(UUID.randomUUID(), "John Doe", null, "123456789", nextMonday, Duration.ofMinutes(30));
            when(appointmentRepository.findForShopId(shopId)).thenReturn(List.of(bookedAppointment));

            // when
            List<LocalTime> availableTimes = appointmentScheduling.availableDatesOnDay(shopId, DayOfWeek.MONDAY);

            // then
            List<LocalTime> expectedTimes = List.of(
                    LocalTime.of(9, 30),
                    LocalTime.of(10, 0),
                    LocalTime.of(10, 30),
                    LocalTime.of(11, 0),
                    LocalTime.of(11, 30),
                    LocalTime.of(12, 0),
                    LocalTime.of(12, 30),
                    LocalTime.of(13, 0),
                    LocalTime.of(13, 30),
                    LocalTime.of(14, 0),
                    LocalTime.of(14, 30),
                    LocalTime.of(15, 0),
                    LocalTime.of(15, 30),
                    LocalTime.of(16, 0),
                    LocalTime.of(16, 30)
            );

            assertEquals(expectedTimes, availableTimes);
        }
    }
}