package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.model.reporting.ShopReport;
import de.hda.fbi.efussgaengerzone.domain.model.shop.VideoMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class Reporting {
    private static final Logger LOG = LoggerFactory.getLogger(Reporting.class);
    private final AppointmentRepository appointmentRepository;

    public Reporting(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }


    public ShopReport getShopReport(UUID id) {
        LOG.info("Generating shop report for shopId {}", id);
        Collection<Appointment> appointments = appointmentRepository.findForShopId(id);

        LocalDate now = LocalDate.now();
        LocalDate lastWeekStart = now.minusDays(7).with(ChronoField.DAY_OF_WEEK, 1).minusDays(1);
        LocalDate thisWeekStart = now.with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate nextWeekStart = now.plusDays(7).with(ChronoField.DAY_OF_WEEK, 1);
        LocalDate weekAfterNextStart = now.plusDays(14).with(ChronoField.DAY_OF_WEEK, 1);

        long appointmentsLastWeek = appointments.stream()
                .filter(a -> a.dateTime().toLocalDate().isAfter(lastWeekStart) && a.dateTime().toLocalDate().isBefore(thisWeekStart))
                .count();
        long appointmentsCurrentWeek = appointments.stream()
                .filter(a -> a.dateTime().toLocalDate().isAfter(thisWeekStart.minusDays(1)) && a.dateTime().toLocalDate().isBefore(nextWeekStart))
                .count();
        long appointmentsNextWeek = appointments.stream()
                .filter(a -> a.dateTime().toLocalDate().isAfter(nextWeekStart.minusDays(1)) && a.dateTime().toLocalDate().isBefore(weekAfterNextStart))
                .count();
        Set<String> uniqueCustomers = appointments.stream()
                .map(Appointment::customerName)
                .collect(Collectors.toSet());
        long totalCustomers = uniqueCustomers.size();

        Map<VideoMessenger, Long> videoMessengerCount = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::chosenMessenger, Collectors.counting()));

        Optional<VideoMessenger> mostPreferredMessenger = videoMessengerCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        LOG.info("Shop report generated for shopId {}", id);
        return new ShopReport(appointmentsLastWeek, appointmentsCurrentWeek, appointmentsNextWeek, mostPreferredMessenger, totalCustomers);
    }
}
