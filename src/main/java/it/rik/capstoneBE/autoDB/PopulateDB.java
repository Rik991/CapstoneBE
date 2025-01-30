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
        if(vehicleRepository.count() == 0){
            try (InputStream inputStream = getClass().getResourceAsStream("/data/vehicle.json")) {
                List<Vehicle> vehicles = objectMapper.readValue(inputStream, new TypeReference<List<Vehicle>>() {});
                for (Vehicle vehicle : vehicles) {
                    vehicleRepository.save(vehicle); // Salva il veicolo con i tipi di motore
                }
                System.out.println("Vehicles loaded successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}