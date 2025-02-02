package it.rik.capstoneBE.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.marca = :marca ORDER BY v.marca ASC")
    List<Vehicle> findByMarca(String marca);

    @Query("SELECT DISTINCT v.marca FROM Vehicle v ORDER BY v.marca ASC")
    List<String> findAllMarche();

}