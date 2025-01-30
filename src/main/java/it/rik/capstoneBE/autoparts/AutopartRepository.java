package it.rik.capstoneBE.autoparts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AutopartRepository extends JpaRepository<Autopart, Long> {

    //findbycodiceoe
    Optional<Autopart> findByCodiceOe(String codiceOe);


}



