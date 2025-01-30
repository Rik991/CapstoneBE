package it.rik.capstoneBE.autoparts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AutopartRepository extends JpaRepository<Autopart, Long> {

    Optional<Autopart> findByCodiceOe(String codiceOe);

    @Query("SELECT DISTINCT a FROM Autopart a")
    List<Autopart> findAllAutoparts();


}



