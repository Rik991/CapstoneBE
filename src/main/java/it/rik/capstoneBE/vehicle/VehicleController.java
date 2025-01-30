package it.rik.capstoneBE.vehicle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/marche")
    public ResponseEntity<List<String>> getAllMarche(){
        return ResponseEntity.ok(vehicleService.getAllMarche());
    }

    @GetMapping("/bymarca")
    public ResponseEntity<List<Vehicle>> getAllByMarca(@RequestParam String marca) {
        return ResponseEntity.ok(vehicleService.getAllByMarca(marca));
    }



}
