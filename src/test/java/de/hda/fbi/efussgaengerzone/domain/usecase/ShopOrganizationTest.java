package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShopOrganizationTest {

    private static final Shop SAMPLE_SHOP = new Shop(UUID.randomUUID(), "name", "description", Set.of(), null, Set.of(), false, 15, "test@email.com");

    private final ShopRepository shopRepository = mock(ShopRepository.class);
    private final ShopOrganization sut = new ShopOrganization(shopRepository);

    @Test
    void createShop() {
        Shop shop = new Shop(UUID.randomUUID(), "name", "description", Set.of(), null, Set.of(), false, 15, "");
            sut.createShop(shop);
            verify(shopRepository).save(shop);
        }


    @Nested
    class changeShop {

        @Test
        void whenTheShopExistsItIsChangedSuccessfully() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.of(SAMPLE_SHOP));

            sut.changeShop(SAMPLE_SHOP);

            verify(shopRepository).save(SAMPLE_SHOP);
        }

        @Test
        void whenThereIsNoShopTheShopCannotBeChanged() {
            when(shopRepository.findById(SAMPLE_SHOP.id())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sut.changeShop(SAMPLE_SHOP))
                    .isInstanceOf(ShopNotFoundException.class);
        }
    }


}