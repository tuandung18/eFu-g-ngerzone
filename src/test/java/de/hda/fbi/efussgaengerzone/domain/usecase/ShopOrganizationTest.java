package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.OpeningHours;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.WeeklyOpeningHours;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopOrganizationTest {

    private static final Shop SAMPLE_SHOP = new Shop(UUID.randomUUID(), "name", "description", Set.of(), null, Set.of(), false, 15, "test@email.com");

    private final ShopRepository shopRepository = mock(ShopRepository.class);
    private final ShopOrganization shopOrganization = new ShopOrganization(shopRepository);

    @Test
    void createShop() {
        Shop shop = new Shop(UUID.randomUUID(), "name", "description", Set.of(), null, Set.of(), false, 15, "");
            shopOrganization.createShop(shop);
            verify(shopRepository).save(shop);
        }


    @Nested
    class changeShop {

        @Test
        void whenTheShopExistsItIsChangedSuccessfully() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.of(SAMPLE_SHOP));

            shopOrganization.changeShop(SAMPLE_SHOP);

            verify(shopRepository).save(SAMPLE_SHOP);
        }

        @Test
        void whenThereIsNoShopTheShopCannotBeChanged() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> shopOrganization.changeShop(SAMPLE_SHOP))
                    .isInstanceOf(ShopNotFoundException.class);
        }
    }

    @Nested
    class updateShopEmail {
        @Test
        void whenTheShopExistsTheEmailIsChangedSuccessfully() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.of(SAMPLE_SHOP));

            String newEmail = "new@email.com";
            shopOrganization.updateShopEmail(SAMPLE_SHOP.id(), newEmail);

            ArgumentCaptor<Shop> shopCaptor = ArgumentCaptor.forClass(Shop.class);
            verify(shopRepository).save(shopCaptor.capture());
            verify(shopRepository, times(1)).save(any());
            assertThat(shopCaptor.getValue()).extracting(Shop::ownerEmail).isEqualTo(newEmail);
        }

        @Test
        void whenTheShopDoesntExistAnErrorIsThrown() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.of(SAMPLE_SHOP));

            UUID nonExistingShopId = UUID.randomUUID();
            Throwable exception = assertThrows(ShopNotFoundException.class, () ->
                    shopOrganization.updateShopEmail(nonExistingShopId, "new@email.com"));
            assertThat(exception).hasMessageContaining(nonExistingShopId.toString());
        }
    }
}