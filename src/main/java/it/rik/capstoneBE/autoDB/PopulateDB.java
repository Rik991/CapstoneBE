package it.rik.capstoneBE.autoDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.auth.RegisterRequest;
import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartRepository;
import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.price.PriceRepository;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PopulateDB {

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private AutopartRepository autopartRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void seedDatabase() {
        loadVehicles();
        loadResellers();
        loadAutoparts();
        loadPrices();
    }

    private void loadVehicles() {
        if (vehicleRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/vehicle.json")) {
                if (inputStream == null) throw new RuntimeException("File 'vehicle.json' non trovato");
                List<Vehicle> vehicles = objectMapper.readValue(inputStream, new TypeReference<>() {});
                vehicleRepository.saveAll(vehicles);
                System.out.println("Database popolato con veicoli di esempio.");
            } catch (Exception e) {
                System.err.println("Errore durante il popolamento dei veicoli: " + e.getMessage());
            }
        }
    }

    private void loadResellers() {
        if (resellerRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                Optional<User> resellerUser = userService.findByUsername("reseller" + i);
                if (resellerUser.isEmpty()) {
                    RegisterRequest resellerRequest = new RegisterRequest();
                    resellerRequest.setUsername("reseller" + i);
                    resellerRequest.setPassword("resellerpwd" + i);
                    resellerRequest.setEmail("reseller@epicode.it" + i);
                    resellerRequest.setName("Reseller" + i);
                    resellerRequest.setSurname("Venditore" + i);
                    resellerRequest.setRagioneSociale("Reseller SRL" + i);
                    resellerRequest.setPartitaIva("12345678901" + i);
                    userService.registerReseller(resellerRequest, null);
                }
            }
            System.out.println("Database popolato con 5 rivenditori di esempio.");
        }
    }

    private void loadAutoparts() {
        if (autopartRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/autoparts.json")) {
                if (inputStream == null) throw new RuntimeException("File 'autoparts.json' non trovato");

                List<Map<String, Object>> rawParts = objectMapper.readValue(inputStream, new TypeReference<>() {});
                List<Autopart> autopartList = new ArrayList<>();

                for (Map<String, Object> rawPart : rawParts) {
                    Autopart autopart = new Autopart();
                    autopart.setNome((String) rawPart.get("nome"));
                    autopart.setCodiceOe((String) rawPart.get("codiceOe"));
                    autopart.setCategoria((String) rawPart.get("categoria"));

                    List<Integer> vehicleIds = (List<Integer>) rawPart.get("veicoliCompatibili");
                    List<Long> vehicleIdsLong = vehicleIds.stream().map(Integer::longValue).collect(Collectors.toList());
                    Set<Vehicle> vehicles = new HashSet<>(vehicleRepository.findAllById(vehicleIdsLong));


                    autopart.setVeicoliCompatibili(vehicles);

                    autopartList.add(autopart);
                }

                autopartRepository.saveAll(autopartList);
                System.out.println("Database popolato con ricambi di esempio.");
            } catch (Exception e) {
                System.err.println("Errore durante il popolamento dei ricambi: " + e.getMessage());
            }
        }
    }

    private void loadPrices() {
        if (priceRepository.count() == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/data/prices.json")) {
                if (inputStream == null) throw new RuntimeException("File 'prices.json' non trovato");

                List<Map<String, Object>> rawPrices = objectMapper.readValue(inputStream, new TypeReference<>() {});
                List<Price> pricesList = new ArrayList<>();

                for (Map<String, Object> rawPrice : rawPrices) {
                    Price price = new Price();
                    price.setPrezzo((Double) rawPrice.get("prezzo"));

                    int autopartsId = (Integer) rawPrice.get("autopartsId");
                    int venditoreId = (Integer) rawPrice.get("venditoreId");

                    Autopart autopart = autopartRepository.findById((long) autopartsId)
                            .orElseThrow(() -> new RuntimeException("Autoparts ID " + autopartsId + " non trovato"));
                    Reseller reseller = resellerRepository.findById((long) venditoreId)
                            .orElseThrow(() -> new RuntimeException("Venditore ID " + venditoreId + " non trovato"));

                    price.setAutopart(autopart);
                    price.setVenditore(reseller);
                    pricesList.add(price);
                }

                priceRepository.saveAll(pricesList);
                System.out.println("Database popolato con prezzi di esempio.");
            } catch (Exception e) {
                System.err.println("Errore durante il popolamento dei prezzi: " + e.getMessage());
            }
        }
    }
}