package it.rik.capstoneBE.autoDB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.auth.RegisterRequest;
import it.rik.capstoneBE.autoparts.AutopartRepository;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

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
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {

        loadVehicles();

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