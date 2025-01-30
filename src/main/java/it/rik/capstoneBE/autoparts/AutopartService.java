// AutopartService.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.price.PriceRepository;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AutopartService {

    @Autowired
    private AutopartRepository autopartRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ResellerRepository resellerRepository;



    @Transactional
    public Autopart createAutopartWithPrice(AutopartPriceRequestDTO request) {
        // Trova il venditore
        Reseller venditore = resellerRepository.findById(request.getVenditoreId())
                .orElseThrow(() -> new IllegalArgumentException("Venditore non trovato"));
        // Trova i veicoli compatibili
        Set<Vehicle> veicoliCompatibili = request.getVeicoliCompatibiliIds().stream()
                .map(vehicleId -> vehicleRepository.findById(vehicleId)
                        .orElseThrow(() -> new IllegalArgumentException("Veicolo non trovato con ID: " + vehicleId)))
                .collect(Collectors.toSet());


        // Crea l'Autopart
        Autopart autopart = new Autopart();
        autopart.setNome(request.getNome());
        autopart.setCodiceOe(request.getCodiceOe());
        autopart.setDescrizione(request.getDescrizione());
        autopart.setCategoria(request.getCategoria());
        autopart.setCondizione(request.getCondizione());
        autopart.setImmagine(request.getImmagine());
        autopart.setVeicoliCompatibili(veicoliCompatibili);
        autopartRepository.save(autopart);

        // Crea il Price
        Price price = new Price();
        price.setPrezzo(request.getPrezzo());
        price.setAutopart(autopart);
        price.setVenditore(venditore);
        priceRepository.save(price);

        return autopart;
    }
}