package it.rik.capstoneBE.autoDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.auth.RegisterRequest;
import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartRepository;
import it.rik.capstoneBE.autoparts.Condizione;
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
    public void init() {
        loadResellers();
        loadVehicles();
        loadAutoparts();
        loadPrices();
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
                    resellerRequest.setSitoWeb("www.reseller" + i + ".it");
                    userService.registerReseller(resellerRequest, null);
                }
            }
            System.out.println("Database popolato con 5 rivenditori di esempio.");
        }
    }
    private void loadVehicles() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/vehicle.json")) {
            List<Vehicle> vehicles = objectMapper.readValue(inputStream, new TypeReference<List<Vehicle>>() {});
            vehicleRepository.saveAll(vehicles);
            System.out.println("Vehicles loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAutoparts() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/autoparts.json")) {
            List<Autopart> autoparts = objectMapper.readValue(inputStream, new TypeReference<List<Autopart>>() {});
            for (Autopart autopart : autoparts) {
                Optional<Autopart> existingAutopart = autopartRepository.findByCodiceOe(autopart.getCodiceOe());
                if (existingAutopart.isPresent()) {
                    System.out.println("Autopart with codice_oe " + autopart.getCodiceOe() + " already exists. Skipping insertion.");
                    continue;
                }
                Set<Long> vehicleIds = Optional.ofNullable(autopart.getVeicoliCompatibiliIds()).orElse(Collections.emptySet());
                Set<Vehicle> compatibleVehicles = vehicleIds.stream()
                        .map(vehicleId -> vehicleRepository.findById(vehicleId).orElse(null))
                        .collect(Collectors.toSet());
                autopart.setVeicoliCompatibili(compatibleVehicles);
                autopartRepository.save(autopart);
            }
            System.out.println("Autoparts loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPrices() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/prices.json")) {
            List<Price> prices = objectMapper.readValue(inputStream, new TypeReference<List<Price>>() {});
            for (Price price : prices) {
                Optional<Autopart> autopart = autopartRepository.findById(price.getAutopartsId());
                Optional<Reseller> reseller = resellerRepository.findById(price.getVenditoreId());
                if (autopart.isPresent() && reseller.isPresent()) {
                    price.setAutopart(autopart.get());
                    price.setVenditore(reseller.get());
                    priceRepository.save(price);
                } else {
                    System.out.println("Autopart or Reseller not found for price with id " + price.getId());
                }
            }
            System.out.println("Prices loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}