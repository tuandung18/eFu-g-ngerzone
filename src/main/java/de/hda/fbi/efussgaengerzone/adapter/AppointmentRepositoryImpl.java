package de.hda.fbi.efussgaengerzone.adapter;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.usecase.AppointmentNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final Map<UUID, List<Appointment>> appointmentsByShop = new HashMap<>();

    @Override
    public Collection<Appointment> findForShopId(UUID shopId) {
        return Set.of();
    }

    @Override
    public Appointment save(UUID shopId, Appointment newAppointment) {
        return null;
    }

    @Override
    public Appointment delete(UUID shopId, UUID appointmentId) {
        List<Appointment> shopAppointments = appointmentsByShop.getOrDefault(shopId, new ArrayList<>());

        Appointment deletedAppointment = null;
        Iterator<Appointment> it = shopAppointments.iterator();
        while (it.hasNext()) {
            Appointment appointment = it.next();
            if (appointmentId.equals(appointment.id())) {
                it.remove();
                deletedAppointment = appointment;
            }
        }

        if (deletedAppointment != null) {
            if (shopAppointments.isEmpty()) {
                appointmentsByShop.remove(shopId);
            }
            return deletedAppointment;
        } else {
            throw new AppointmentNotFoundException(String.format("Appointment %s not found for shop %s", appointmentId, shopId));
        }
    }
}
