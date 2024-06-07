package de.hda.fbi.efussgaengerzone.adapter;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.usecase.AppointmentNotFoundException;
import org.springframework.stereotype.Repository;
<<<<<<< src/main/java/de/hda/fbi/efussgaengerzone/adapter/AppointmentRepositoryImpl.java

import java.util.*;
=======
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
>>>>>>> src/main/java/de/hda/fbi/efussgaengerzone/adapter/AppointmentRepositoryImpl.java

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final Map<UUID, List<Appointment>> appointmentsByShop = new HashMap<>();

    @Override
    public Collection<Appointment> findForShopId(UUID shopId) {
        return appointmentsByShop.getOrDefault(shopId, new ArrayList<>());
    }

    @Override
    public Appointment save(UUID shopId, Appointment newAppointment) {
        appointmentsByShop.computeIfAbsent(shopId, k -> new ArrayList<>()).add(newAppointment);
        return newAppointment;
    }

    @Override
    public Appointment delete(UUID shopId, UUID appointmentId) {
<<<<<<< src/main/java/de/hda/fbi/efussgaengerzone/adapter/AppointmentRepositoryImpl.java
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
=======
        List<Appointment> shopAppointments = appointmentsByShop.getOrDefault(shopId,
                new ArrayList<>());
        Appointment appointment = shopAppointments.stream()
                .filter(a -> appointmentId.equals(a.id()))
                .findFirst()
                .orElseThrow(() -> new
                        AppointmentNotFoundException(String.format("Appointment %s not found for shop %s",
                        appointmentId, shopId)));
        shopAppointments.remove(appointment);
        if (shopAppointments.isEmpty()) {
                appointmentsByShop.remove(shopId);
            }
        return appointment;
>>>>>>> src/main/java/de/hda/fbi/efussgaengerzone/adapter/AppointmentRepositoryImpl.java
    }
}
