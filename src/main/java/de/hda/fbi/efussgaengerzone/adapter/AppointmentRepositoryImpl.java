package de.hda.fbi.efussgaengerzone.adapter;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final Map<UUID, List<Appointment>> appointmentsByShop = new HashMap<>();

    @Override
    public Collection<Appointment> findForShopId(UUID shopId) {
        return appointmentsByShop.getOrDefault(shopId, new ArrayList<>())
    }

    @Override
    public Appointment save(UUID shopId, Appointment newAppointment) {
        appointmentsByShop.computeIfAbsent(shopId, k -> new ArrayList<>()).add(newAppointment);
        return newAppointment;
    }

    @Override
    public Appointment delete(UUID shopId, UUID appointmentId) {
        return null;
    }
}
