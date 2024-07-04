package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShopOrganization {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ShopOrganization.class);

    final ShopRepository shopRepository;

    public ShopOrganization(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public void createShop(Shop shop) {
        LOG.debug("Create shop {}", shop);
        shopRepository.save(shop);
    }

    public void updateShopEmail(UUID shopId, String newMail) {
        Shop oldShop = shopRepository.findById(shopId).orElse(null);

        if (oldShop == null) {
            throw new ShopNotFoundException(shopId);
        }

        LOG.info("Update shop {} with Email {} to Email {}", shopId, oldShop.ownerEmail(), newMail);

        Shop newShop = new Shop(shopId, oldShop.name(), oldShop.description(), oldShop.supportedVideoMessengers(),
                oldShop.weeklyOpeningHours(), oldShop.tags(), oldShop.active(), oldShop.minsPerCustomer(), newMail);
        changeShop(newShop);
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

