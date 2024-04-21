package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShopOrganization {

    final ShopRepository shopRepository;

    public ShopOrganization(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void createShop(Shop shop) {
    }

    public void updateShopEmail(UUID shopId, String newMail) {
    }

    public void changeShop(Shop shop) {
        Optional<Shop> existingShop = shopRepository.findById(shop.id());

        if (existingShop.isEmpty()) {
            throw new ShopNotFoundException(shop.id());
        }

        // save creates the shop or updates an existing one
        shopRepository.save(shop);
    }

}
