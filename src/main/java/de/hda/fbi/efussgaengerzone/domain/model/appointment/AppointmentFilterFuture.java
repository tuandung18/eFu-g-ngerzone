package de.hda.fbi.efussgaengerzone.domain.model.appointment;

import java.time.LocalDateTime;

public class AppointmentFilterFuture implements AppointmentFilter {

    public static final AppointmentFilterFuture INSTANCE = new AppointmentFilterFuture();

    private AppointmentFilterFuture() {

    }

    @Override
    public boolean test(Appointment appointment) {
        return appointment.dateTime().isAfter(LocalDateTime.now());
    }
}
