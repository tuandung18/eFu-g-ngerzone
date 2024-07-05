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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private static boolean isC(Set<String> c, String cn) {
        boolean f = false;
        for (String s : c) {
            if (s.equals(cn)) {
                f = true;
            }
        }
        return f;
    }


    public ShopReport getShopReport(UUID id) {
        LOG.info("Generating shop report for shopId {}", id);
        Collection<Appointment> appointments = appointmentRepository.findForShopId(id);

        List<Appointment> l = appointments.stream().toList();
        long aLW = 0;
        long aCW = 0;
        long aNW = 0;
        long tC = 0;
        Set<String> c = new HashSet<>(); // creates a new set
        Map<VideoMessenger, Long> vm = new HashMap<>();
        for (int i = 0; i < appointments.size(); i++) {
            Appointment x = l.get(i);

            if (x.dateTime().toLocalDate().isAfter(LocalDate.now().minusDays(7).with(ChronoField.DAY_OF_WEEK, 1).minusDays(1)) && x.dateTime().toLocalDate().isBefore(LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1))) {
                aLW++;
            }
            if (x.dateTime().toLocalDate().isBefore(LocalDate.now().plusDays(7).with(ChronoField.DAY_OF_WEEK, 1)) && x.dateTime().toLocalDate().isAfter(LocalDate.now().with(ChronoField.DAY_OF_WEEK, 1).minusDays(1))) {
                aCW++;
            }
            if (x.dateTime().toLocalDate().isBefore(LocalDate.now().plusDays(14).with(ChronoField.DAY_OF_WEEK, 1)) && x.dateTime().toLocalDate().isAfter(LocalDate.now().plusDays(7).with(ChronoField.DAY_OF_WEEK, 1).minusDays(1))) {
                aNW++;
            }
            if (!isC(c, x.customerName())) {
                tC++;
                c.add(x.customerName());
            }
            VideoMessenger m = x.chosenMessenger();
            if (vm.containsKey(m)) {
                Long vmc = vm.get(m);
                vm.put(m, Long.valueOf(vmc + 1));
            } else {
                vm.put(m, 1l);
            }
        }

        Map.Entry<VideoMessenger, Long> mpVm = null;
        for (Map.Entry<VideoMessenger, Long> e : vm.entrySet()) {
            if (mpVm == null) {
                mpVm = e;
                continue;
            }
            if (mpVm.getValue() > e.getValue()) {
                continue;
            }
            mpVm = e;

        }
        LOG.info("Shop report generated for shopId {}", id);
        return new ShopReport(aLW, aCW, aNW, mpVm == null ? Optional.empty() : Optional.of(mpVm.getKey()), tC);
    }

}

