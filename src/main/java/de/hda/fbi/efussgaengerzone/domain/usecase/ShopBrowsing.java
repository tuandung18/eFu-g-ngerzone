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

// Service-Komponente im Spring Framework zur Bereitstellung von Shop-bezogenen Geschäftslogiken.
@Service
public class ShopBrowsing {

    // Logger für das Logging von Informationen und Fehlern.
    private static final Logger LOG = LoggerFactory.getLogger(ShopBrowsing.class);
    private final ShopRepository shopRepository; // Repository zum Zugriff auf Shop-Daten.

    // Konstruktor, der das ShopRepository injiziert.
    public ShopBrowsing(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    // Methode zum Abrufen eines Shops basierend auf der E-Mail-Adresse des Besitzers.
    public Shop getShopByOwner(String ownerEmail) {
        LOG.info("Looking up shop for owner {}", ownerEmail); // Loggt den Suchvorgang.
        // Findet alle Shops, deren Eigentümer-E-Mail mit der angegebenen übereinstimmt.
        Set<Shop> shops = shopRepository.findPredicate(shop -> shop.ownerEmail().equals(ownerEmail));
        if (shops.isEmpty()) { // Prüft, ob kein Shop gefunden wurde.
            LOG.info("Shop not found for owner {}", ownerEmail); // Loggt die Information, dass kein Shop gefunden wurde.
            throw new ShopNotFoundException(ownerEmail); // Wirft eine benutzerdefinierte Ausnahme.
        }
        return shops.stream().findFirst().orElseThrow(); // Gibt den ersten gefundenen Shop zurück oder wirft eine Ausnahme.
    }

    // Methode, die alle Shops zurückgibt.
    public Set<Shop> findAll() {
        return shopRepository.findAll();
    }

    // Methode, die Shops basierend auf Suchbegriffen und Tags findet.
    public Set<Shop> findShopsByQuery(Set<String> words, Set<Tag> tags) {
        return shopRepository.findShopsByNames(words);
    }


    // Methode, die einen Shop anhand seiner UUID findet.
    public Optional<Shop> findShopById(UUID uuid) {
        LOG.info("Looking up shop for id {}", uuid); // Loggt den Suchvorgang.
        return shopRepository.findById(uuid); // Ruft das Shop-Repository auf, um den Shop anhand der ID zu finden.
    }
}
