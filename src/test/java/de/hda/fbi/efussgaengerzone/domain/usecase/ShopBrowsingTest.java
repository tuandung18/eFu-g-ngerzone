package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.OpeningHours;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Tag;
import de.hda.fbi.efussgaengerzone.domain.model.shop.VideoMessenger;
import de.hda.fbi.efussgaengerzone.domain.model.shop.WeeklyOpeningHours;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ShopBrowsingTest {

    @Mock
    private ShopRepository repository;

    @InjectMocks
    private ShopBrowsing shopBrowsing;

    final OpeningHours nineToFive = new OpeningHours(LocalTime.of(7, 0), LocalTime.of(16, 0));
    final WeeklyOpeningHours usualWeeklyOpeningHours = new WeeklyOpeningHours(nineToFive, nineToFive, nineToFive, nineToFive, nineToFive, null);

    final private Shop wineAndCoffee = new Shop(
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
            "test@owner.de"
    );


    // im Ausgangsprojekt belassen
    @Nested
    class GetShopByOwner {
        @Test
        void whenShopExistsForOwnerEmailThenReturnIt() {
            // given
            given(repository.findPredicate(any())).willReturn(Set.of(wineAndCoffee));

            // when
            var shop = shopBrowsing.getShopByOwner(wineAndCoffee.ownerEmail());

            // then
            assertThat(shop).isEqualTo(wineAndCoffee);
        }

        @Test
        void whenShopDoesNotExistThrowError() {
            // given
            given(repository.findPredicate(any())).willReturn(emptySet());

            // when
            assertThatThrownBy(() -> shopBrowsing.getShopByOwner("unknown-email"))
                    .isInstanceOf(ShopNotFoundException.class);
        }
    }

}