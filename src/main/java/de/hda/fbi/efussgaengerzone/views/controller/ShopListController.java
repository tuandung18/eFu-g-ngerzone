package de.hda.fbi.efussgaengerzone.views.controller;

import de.hda.fbi.efussgaengerzone.domain.model.shop.Tag;
import de.hda.fbi.efussgaengerzone.domain.usecase.ShopBrowsing;
import de.hda.fbi.efussgaengerzone.views.model.ShopDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ShopListController {

    private final ShopBrowsing shopBrowsing; // Service zur Abfrage von Ladeninformationen aus der Datenbank.

    // Konstruktor für den Controller, der den ShopBrowsing Service injiziert.
    public ShopListController(ShopBrowsing shopBrowsing) {
        this.shopBrowsing = shopBrowsing;
    }

    // HTTP GET-Handler, der die Hauptseite der Ladenliste anzeigt.
    @GetMapping("/")
    public ModelAndView listView(@ModelAttribute("searchText") String searchText) {
        List<ShopDto> shops; // Liste zur Speicherung der Läden, die angezeigt werden sollen.

        // Überprüft, ob der Suchtext leer ist. Wenn ja, werden alle Läden abgerufen.
        if (searchText == null || searchText.equals("")) {
            shops = ShopDto.fromShops(shopBrowsing.findAll());
        } else {
            // Trennt den Suchtext in Tags und normale Wörter.
            var tags = Stream.of(searchText.split(" "))
                    .filter(w -> w.startsWith("#")) // Filtert Wörter, die mit '#' beginnen.
                    .map(w -> Tag.of(w.substring(1))) // Entfernt '#' und erstellt Tag-Objekte.
                    .collect(Collectors.toSet());

            var words = Stream.of(searchText.split(" "))
                    .filter(w -> !w.startsWith("#")) // Filtert Wörter, die nicht mit '#' beginnen.
                    .collect(Collectors.toSet());

            // Ruft die Shops ab, die den Suchkriterien entsprechen.
            shops = ShopDto.fromShops(shopBrowsing.findShopsByQuery(words, tags));
        }

        // Erstellt ein ModelAndView-Objekt, das die Ansicht und die Daten für die Ansicht bereitstellt.
        return new ModelAndView("shopList", ViewConstants.MODEL_NAME, new ListViewModel(
                shops,
                searchText
        ));
    }

    // Hilfsklassen-Record zur Speicherung von Daten, die an die Ansicht übergeben werden.
    record ListViewModel(List<ShopDto> shops, String searchText) {
    }
}
