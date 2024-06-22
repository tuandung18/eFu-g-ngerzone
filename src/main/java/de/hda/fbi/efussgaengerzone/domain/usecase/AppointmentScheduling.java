package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentFilter;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AppointmentScheduling {
    private static final Logger LOG = LoggerFactory.getLogger(Reporting.class);

    private final AppointmentRepository appointmentRepository;
    private final ShopRepository shopRepository;

    public AppointmentScheduling(AppointmentRepository appointmentRepository, ShopRepository shopRepository) {
        this.appointmentRepository = appointmentRepository;
        this.shopRepository = shopRepository;
    }

    public void makeAppointment(UUID shopId, Appointment appointment) {
        LOG.info("Making appointment for shopId {} with details {}", shopId, appointment);
        appointmentRepository.save(shopId, appointment);
        LOG.info("Appointment made successfully for shopId {}", shopId);
    }

    public void deleteAppointment(UUID shopId, UUID appointmentId) {
        LOG.info("Deleting appointment with id {} for shopId {}", appointmentId, shopId);
        appointmentRepository.delete(shopId, appointmentId);
        LOG.info("Appointment with id {} deleted successfully for shopId {}", appointmentId, shopId);
    }

    public Optional<Appointment> findNextAppointment(UUID shopId) {
        LOG.info("Looking up next appointment for shopId {}", shopId);
    }

    public Collection<Appointment> searchAppointments(UUID shopId, Set<AppointmentFilter> filters) {
        LOG.info("Searching appointments for shopId {} with filters {}", shopId, filters);
        return shopRepository
                .findById(shopId)
                .map(shop -> appointmentRepository
                        .findForShopId(shopId)
                        .stream()
                        .filter(appointment -> filters
                                .stream()
                                .allMatch(filter -> filter.test(appointment)))
                        .toList())
                .orElseGet(List::of);
    }

    public List<LocalTime> availableDatesOnDay(UUID shopid, DayOfWeek dayOfWeek) throws ShopNotFoundException {
        LOG.info("Checking available dates on day {} for shopId {}", dayOfWeek, shopid);
        if (shopRepository.findById(shopid).isEmpty()) {
            LOG.error("Shop not found for id {}", shopid);
            throw new ShopNotFoundException(shopid);
        }
        LOG.info("Found {} available dates for shopId {}", List.of().size(), shopid);

        return List.of();
    }

}
