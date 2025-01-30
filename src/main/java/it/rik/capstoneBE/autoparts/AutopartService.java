// AutopartService.java
package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.mapper.AutopartMapper;
import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.price.PriceRepository;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerInfoDTO;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AutopartService {

    @Autowired
    private AutopartRepository autopartRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private AutopartMapper autopartMapper;

    public List<AutopartResponseDTO> getAllAutoparts() {
        List<Autopart> autoparts = autopartRepository.findAllAutoparts();
        List<Price> allPrices = priceRepository.findAllPricesWithResellers();

        Map<Long, List<Price>> pricesByAutopartId = allPrices.stream()
                .collect(Collectors.groupingBy(price -> price.getAutopart().getId()));

        return autoparts.stream()
                .map(autopart -> autopartMapper.toDTO(
                        autopart,
                        pricesByAutopartId.getOrDefault(autopart.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }


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