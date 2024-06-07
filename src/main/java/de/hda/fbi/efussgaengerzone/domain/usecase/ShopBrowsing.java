package de.hda.fbi.efussgaengerzone.domain.usecase;

import de.hda.fbi.efussgaengerzone.domain.model.shop.Shop;
import de.hda.fbi.efussgaengerzone.domain.model.shop.ShopRepository;
import de.hda.fbi.efussgaengerzone.domain.model.shop.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ShopBrowsing {

    private static final Logger LOG = LoggerFactory.getLogger(ShopBrowsing.class);
    private final ShopRepository shopRepository;

    /**
     * Konstruktor: erstellt eine neue Instanz von ShopBrowsing mit dem angegebenen ShopRepository.
     *
     *  @param shopRepository Das Repository zum Zugriff auf Shop-Daten.
     */
    public ShopBrowsing(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    /**
     * Diese Methode verwendet die angegebene E-Mail-Adresse, um nach Shops zu suchen,
     * deren Besitzer diese E-Mail-Adresse haben. Wenn kein Shop gefunden wird, wird eine
     * Ausnahme ausgelöst.
     *
     * @param ownerEmail Die E-Mail-Adresse des Shop-Besitzers.
     * @return Der Shop, dessen Eigentümer die angegebene E-Mail-Adresse hat.
     * @throws ShopNotFoundException Wenn kein Shop für die angegebene E-Mail-Adresse gefunden wurde.
     */
    public Shop getShopByOwner(String ownerEmail) {
        LOG.info("Looking up shop for owner {}", ownerEmail);
        Set<Shop> shops = shopRepository.findPredicate(shop -> shop.ownerEmail().equals(ownerEmail));
        if (shops.isEmpty()) {
            LOG.info("Shop not found for owner {}", ownerEmail);
            throw new ShopNotFoundException(ownerEmail);
        }
        return shops.stream().findFirst().orElseThrow();
    }

    /**
     * Diese Methode ruft die findAll Methode beim shopRepository, die alle verfügbaren Shops zurückgibt.
     * Sie ruft alle Shops aus dem Repository ab und gibt sie als Set zurück.
     *
     * @return Ein Set von allen Shops.
     */
    public Set<Shop> findAll() {
        return shopRepository.findAll();
    }

    /**
     * Diese Methode ruft die findShopsByNames Methode beim shopRepository,
     * die die Shops anhand der angegebenen Suchwörter filtert.
     *
     * @param words Die Suchwörter zur Filterung der Shops.
     * @return Ein Set von Shops, die den Suchwörtern entsprechen.
     */
    public Set<Shop> findShopsByQuery(Set<String> words, Set<Tag> tags) {
        return shopRepository.findShopsByNames(words);
    }

    /**
     * Sucht und gibt einen Shop basierend auf seiner UUID zurück.
     *
     * @param uuid Die UUID des Shops.
     * @return Ein Optional, das den gefundenen Shop enthält, oder ein leeres Optional, wenn kein Shop gefunden wurde.
     */    public Optional<Shop> findShopById(UUID uuid) {
        LOG.info("Looking up shop for id {}", uuid);
        return shopRepository.findById(uuid);
    }
}