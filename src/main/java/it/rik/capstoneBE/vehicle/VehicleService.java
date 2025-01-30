package it.rik.capstoneBE.vehicle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAllByMarca(String marca) {
        return vehicleRepository.findByMarca(marca);
    }

    public List<String> getAllMarche(){
        return vehicleRepository.findAllMarche();
    }

}
