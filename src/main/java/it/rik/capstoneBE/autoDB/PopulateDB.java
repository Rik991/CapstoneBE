package it.rik.capstoneBE.autoDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.auth.RegisterRequest;
import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartRepository;
import it.rik.capstoneBE.autoparts.Condizione;
import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.price.PriceRepository;
import it.rik.capstoneBE.user.UserService;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PopulateDB {

    @Autowired private UserService userService;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private ResellerRepository resellerRepository;
    @Autowired private AutopartRepository autopartRepository;
    @Autowired private PriceRepository priceRepository;

    private ObjectMapper objectMapper;

    public PopulateDB() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @PostConstruct
    public void seedDatabase() {
        try {
            loadVehicles();
            loadResellers();
            loadAutoparts();
            loadPrices();
        } catch (Exception e) {
            log.error("Errore durante il popolamento del database: ", e);
        }
    }

    private void loadVehicles() {
        if (vehicleRepository.count() > 0) {
            log.info("I veicoli sono già presenti nel database");
            return;
        }
        List<Vehicle> vehicles = loadFromJson("/data/vehicle.json", new TypeReference<List<Vehicle>>() {});
        vehicleRepository.saveAll(vehicles);
    }

    private void loadResellers() {
        if (resellerRepository.count() > 0) {
            log.info("I rivenditori sono già presenti nel database");
            return;
        }
        for (int i = 1; i <= 5; i++) {
            String username = "reseller" + i;
            if (userService.findByUsername(username).isEmpty()) {
                RegisterRequest request = createResellerRequest(i);
                userService.registerReseller(request, null);
                log.info("Creato rivenditore: {}", username);
            }
        }
    }

    private RegisterRequest createResellerRequest(int index) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("reseller" + index);
        request.setPassword("resellerpwd" + index);
        request.setEmail("reseller" + index + "@epicode.it");
        request.setName("Reseller" + index);
        request.setSurname("Venditore" + index);
        request.setRagioneSociale("Reseller SRL" + index);
        request.setPartitaIva("1234567890" + index);
        request.setSitoWeb("www.reseller" + index + ".it");
        return request;
    }

    private void loadAutoparts() {
        if (autopartRepository.count() > 0) {
            log.info("I ricambi sono già presenti nel database");
            return;
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/autoparts.json")) {
            if (inputStream == null) {
                throw new RuntimeException("File 'autoparts.json' non trovato nel classpath");
            }

            List<Map<String, Object>> rawParts = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

            List<Autopart> autoparts = rawParts.stream().map(rawPart -> {
                Autopart autopart = new Autopart();
                autopart.setNome((String) rawPart.get("nome"));
                autopart.setCodiceOe((String) rawPart.get("codiceOe"));
                autopart.setCategoria((String) rawPart.get("categoria"));
                autopart.setCondizione(Condizione.valueOf((String) rawPart.get("condizione")));

                List<Integer> vehicleIds = (List<Integer>) rawPart.get("veicoliCompatibili");
                Set<Vehicle> vehicles = vehicleRepository.findAllById(vehicleIds.stream().map(Long::valueOf).toList()).stream().collect(Collectors.toSet());
                autopart.setVeicoliCompatibili(vehicles);

                return autopart;
            }).collect(Collectors.toList());

            autopartRepository.saveAll(autoparts);
            log.info("Caricati {} ricambi nel database", autoparts.size());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura di 'autoparts.json': " + e.getMessage(), e);
        }
    }

    private void loadPrices() {
        if (priceRepository.count() > 0) {
            log.info("I prezzi sono già presenti nel database");
            return;
        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/prices.json")) {
            if (inputStream == null) {
                throw new RuntimeException("File 'prices.json' non trovato nel classpath");
            }

            List<Map<String, Object>> rawPrices = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

            List<Price> prices = new ArrayList<>();

            for (Map<String, Object> rawPrice : rawPrices) {
                Price price = new Price();
                price.setPrezzo(((Number) rawPrice.get("prezzo")).doubleValue());

                Long autopartsId = ((Number) rawPrice.get("autopartsId")).longValue();
                Long venditoreId = ((Number) rawPrice.get("venditoreId")).longValue();

                Optional<Autopart> autopartOpt = autopartRepository.findById(autopartsId);
                Optional<Reseller> resellerOpt = resellerRepository.findById(venditoreId);

                if (autopartOpt.isEmpty()) {
                    log.warn("Autopart con ID {} non trovato, saltando...", autopartsId);
                    continue;
                }
                if (resellerOpt.isEmpty()) {
                    log.warn("Reseller con ID {} non trovato, saltando...", venditoreId);
                    continue;
                }

                price.setAutopart(autopartOpt.get());
                price.setVenditore(resellerOpt.get());
                prices.add(price);
            }

            // Utilizzare una copia della collezione per iterare e salvare gli elementi
            List<Price> pricesCopy = new ArrayList<>(prices);
            for (Price price : pricesCopy) {
                priceRepository.save(price);
            }

            log.info("Caricati {} prezzi nel database", prices.size());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura di 'prices.json': " + e.getMessage(), e);
        }
    }

    private <T> List<T> loadFromJson(String path, TypeReference<List<T>> typeReference) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path.substring(1))) {
            if (inputStream == null) {
                throw new RuntimeException("File non trovato: " + path + " - Controlla che sia in src/main/resources/data/");
            }
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura di " + path + ": " + e.getMessage(), e);
        }
    }
}