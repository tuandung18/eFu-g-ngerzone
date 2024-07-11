package de.hda.fbi.efussgaengerzone;

import de.hda.fbi.efussgaengerzone.domain.model.appointment.Appointment;
import de.hda.fbi.efussgaengerzone.domain.model.appointment.AppointmentRepository;
import de.hda.fbi.efussgaengerzone.domain.model.owner.Owner;
import de.hda.fbi.efussgaengerzone.domain.model.owner.OwnerRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("debug")
public class Startup {
    // Klasse zum Erzeugen von Testdaten
    private static final String CUSTOMER_1 = "Wiebke Wodka";
    private static final String CUSTOMER_2 = "Wilhelm Whiskey";
    private static final String CUSTOMER_3 = "Werner Wein";
    private static final String CUSTOMER_4 = "Walter White";
    private static final String CUSTOMER_5 = "Wolfgang Waldmeisterbowle";
    private static final String CUSTOMER_6 = "Tuan Dung Le";

    private final ShopRepository shopRepository;
    private final OwnerRepository ownerRepository;
    private final AppointmentRepository appointmentRepository;

    public Startup(ShopRepository shopRepository, OwnerRepository ownerRepository, AppointmentRepository appointmentRepository) {
        this.shopRepository = shopRepository;
        this.ownerRepository = ownerRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostConstruct
    public void init() {
        setShopDebugData(createCoffeeShopOwner(), createCigarShopOwner(), createBubbleTeaShopOwner(), createIceCreamShopOwner());
    }

    private Owner createCoffeeShopOwner() {
        // for testing shop with appointment data
        final var coffeeShopOwner = new Owner(
                "Mr. Coffee",
                "owner@coffee.shop",
                "123"
        );

        ownerRepository.save(coffeeShopOwner);
        return coffeeShopOwner;
    }

    private Owner createCigarShopOwner() {
        // for testing shop without appointments
        final var cigarShopOwner = new Owner(
                "Mr. Tobacco",
                "owner@cigar.shop",
                "123"
        );

        ownerRepository.save(cigarShopOwner);
        return cigarShopOwner;
    }

    private Owner createBubbleTeaShopOwner() {
        // for testing shop without appointments
        final var bubbleTeaShopOwner = new Owner(
                "Mr. Bubble",
                "owner@bubble.shop",
                "123"
        );

        ownerRepository.save(bubbleTeaShopOwner);
        return bubbleTeaShopOwner;
    }

    private Owner createIceCreamShopOwner() {
        // for testing shop without appointments
        final var iceCreamShopOwner = new Owner(
                "Mr. Ice",
                "owner@ice.shop@",
                "123"
        );

        ownerRepository.save(iceCreamShopOwner);
        return iceCreamShopOwner;
    }

    private void setShopDebugData(Owner coffeeShopOwner, Owner cigarShopOwner, Owner bubbleTeaShopOwner, Owner iceCreamShopOwner) {
        final var nineToFive = new OpeningHours(LocalTime.of(7, 0), LocalTime.of(16, 0));
        final var usualWeeklyOpeningHours = new WeeklyOpeningHours(nineToFive, nineToFive, nineToFive, nineToFive, nineToFive, null);

        final var coffeeShop = new Shop(
                UUID.randomUUID(),
                "Student Coffee Shop",
                "Lust auf einen Original Mensa Kaffee? Dann bist du in unserem Laden genau richtig! Wir, Felix" +
                        " und Dieter, bringen dir die alte Studentenzeit nach Hause und beliefern dich mit Originalen" +
                        " Kaffesorten aus verschiedenen Mensen Deutschlands. Mit dabei haben wir zum Beispiel den leckeren" +
                        "aus Darmstadt, die liebevoll genannte 'Dunkle Plörre' aus Aachen und den starken aus Gießen." +
                        "Bestell bei uns und dir wird bald genauso schlecht wie früher vom Mensa Essen.",
                Set.of(VideoMessenger.WHATSAPP, VideoMessenger.SKYPE),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("coffee"), Tag.of("cheap")),
                true,
                15,
                coffeeShopOwner.email()
        );

        final var cigarShop = new Shop(
                UUID.randomUUID(),
                "Cigar Shop",
                "Zigarren, Zigarillos und die gelegentliche Tabakpfeife für den feinen Herrn von morgen. " +
                        "Wir konzentrieren uns auf Cubanische, Deutsche und Französische Tabakwaren und bieten Ihnen " +
                        "und Ihren Mitbewohnern den klassischen Kneipengeruch aus den 70er Jahren. Bestellen Sie bei uns" +
                        "und Sie erhalten einen Gutschein über 20 EUR für den ersten Lungenvorsorgetermin dazu.",
                Set.of(VideoMessenger.SKYPE, VideoMessenger.ZOOM),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("cigar"), Tag.of("cigarette"), Tag.of("yolo")),
                true,
                10,
                cigarShopOwner.email()
        );

        final var wineShop = new Shop(
                UUID.randomUUID(),
                "Emmas Fine Wine Shop",
                "Emma war der Name meiner Großmutter. Sie war Gastgeberin durch und durch und ihr Name steht " +
                        "für mich für die Atmosphäre, die wir in dieser Weinbar schaffen wollen – ein Gefühl von" +
                        " „Zuhause“. Ein Platz zum Wohlfühlen und Spaß haben. Ein Ort, an dem man sich mit Freunden" +
                        " trifft, gute Unterhaltungen führt und dazu ein Glas Wein und etwas Leckeres genießt. Aber der " +
                        "Name steht noch für mehr, nämlich für ein „Danke“ an meine Familie, ohne die das EMMAS ein" +
                        " Traum geblieben wäre.",
                Set.of(VideoMessenger.ZOOM),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("wine"), Tag.of("deluxe")),
                true,
                15,
                ""
        );

        final var wineAndCoffee = new Shop(
                UUID.randomUUID(),
                "Wine and Coffee Shop",
                "Unser Name ist Programm. Wir bieten Ihnen Weine von kleinen, aber ausgezeichneten Wein-" +
                        " gütern, von welchen wir unsere Ware direkt beziehen. Der Umweg über den Zwischenhandel " +
                        "entfällt, so dass Spitzenweine zu einem günstigen Preis angeboten werden können.",
                Set.of(VideoMessenger.ZOOM),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("deluxe"), Tag.of("yolo")),
                true,
                10,
                ""
        );


        final var bubbleTeaShop = new Shop(
                UUID.randomUUID(),
                "Bubble Tea Shop",
                "In unserem Bubble Tea Shop möchten wir Ihnen ein einzigartiges Erlebnis bieten. Tauchen Sie ein in die " +
                        "Welt der köstlichen und erfrischenden Bubble Tees, die wir mit hochwertigen Zutaten und frischen Früchten " +
                        "für Sie zubereiten. Wir glauben an die Magie dieser perlenden Getränke und laden Sie ein, sie in " +
                        "verschiedenen Geschmacksrichtungen zu entdecken. Lassen Sie sich von uns verwöhnen und genießen Sie " +
                        "einen Moment der Entspannung bei uns.",
                Set.of(VideoMessenger.INSTAGRAM, VideoMessenger.FACETIME),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("bubble"), Tag.of("erfrischung")),
                true,
                8,
                bubbleTeaShopOwner.email()
        );


        final var iceCreamShop = new Shop(
                UUID.randomUUID(),
                "Ice cream Shop",
                "Willkommen in unserem Eiscafé, wo wir Ihnen eine Vielfalt an cremigen und erfrischenden Eissorten " +
                        "anbieten. Unser Eis wird täglich frisch zubereitet und wir verwenden nur die besten Zutaten, um Ihnen " +
                        "ein unvergessliches Geschmackserlebnis zu garantieren. Ob klassische Sorten oder innovative " +
                        "Kreationen, bei uns finden Sie immer etwas nach Ihrem Geschmack. Genießen Sie eine Auszeit vom " +
                        "Alltag und lassen Sie sich von unserem köstlichen Eis verführen.",
                Set.of(VideoMessenger.INSTAGRAM),
                usualWeeklyOpeningHours,
                Set.of(Tag.of("eis"), Tag.of("genuss")),
                true,
                12,
                iceCreamShopOwner.email()
        );


        shopRepository.clear();
        Set.of(coffeeShop, cigarShop, wineShop, wineAndCoffee, bubbleTeaShop, iceCreamShop)
                .forEach(shopRepository::save);

        final Collection<Appointment> coffeeShopAppointments = List.of(
                new Appointment(UUID.randomUUID(), CUSTOMER_1, VideoMessenger.SKYPE, "wwod", LocalDateTime.now().minusDays(7), Duration.ofMinutes(30)),
                new Appointment(UUID.randomUUID(), CUSTOMER_2, VideoMessenger.ZOOM, "MEETING-1", LocalDateTime.now().minusDays(2), Duration.ofMinutes(15)),
                new Appointment(UUID.randomUUID(), CUSTOMER_1, VideoMessenger.SKYPE, "wwod", LocalDateTime.now(), Duration.ofMinutes(15)),
                new Appointment(UUID.randomUUID(), CUSTOMER_3, VideoMessenger.ZOOM, "w.wein", LocalDateTime.now().plusHours(3), Duration.ofMinutes(15)),
                new Appointment(UUID.randomUUID(), CUSTOMER_4, VideoMessenger.TELEGRAM, "H.Berg", LocalDateTime.now().plusDays(2), Duration.ofMinutes(30)),
                new Appointment(UUID.randomUUID(), CUSTOMER_1, VideoMessenger.SKYPE, "wwod", LocalDateTime.now().plusDays(7), Duration.ofMinutes(30)),
                new Appointment(UUID.randomUUID(), CUSTOMER_5, VideoMessenger.SKYPE, "wwb", LocalDateTime.now().plusDays(7), Duration.ofMinutes(30))
        );
        coffeeShopAppointments.forEach(a -> appointmentRepository.save(coffeeShop.id(), a));

        final Collection<Appointment> bubbleTeaShopAppointments = List.of(
                new Appointment(UUID.randomUUID(), CUSTOMER_6, VideoMessenger.INSTAGRAM, "tdl", LocalDateTime.now().minusDays(7), Duration.ofMinutes(8)),
                new Appointment(UUID.randomUUID(), CUSTOMER_6, VideoMessenger.FACETIME, "tdl", LocalDateTime.now().minusDays(7), Duration.ofMinutes(8))
        );
        bubbleTeaShopAppointments.forEach(a -> appointmentRepository.save(bubbleTeaShop.id(), a));


        final Collection<Appointment> iceCreamShopAppointments = List.of(
                new Appointment(UUID.randomUUID(), CUSTOMER_6, VideoMessenger.INSTAGRAM, "tdl", LocalDateTime.now().minusDays(10), Duration.ofMinutes(12)),
                new Appointment(UUID.randomUUID(), CUSTOMER_6, VideoMessenger.INSTAGRAM, "tdl", LocalDateTime.now().minusDays(10), Duration.ofMinutes(12))
        );
        iceCreamShopAppointments.forEach(a -> appointmentRepository.save(iceCreamShop.id(), a));
    }
}
