package it.rik.capstoneBE.price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    @Query("SELECT DISTINCT p FROM Price p LEFT JOIN FETCH p.venditore LEFT JOIN FETCH p.autopart")
    List<Price> findAllPricesWithResellers();


}