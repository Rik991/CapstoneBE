package it.rik.capstoneBE.autoparts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutopartRepository extends JpaRepository<Autopart, Long> {


    @Query("SELECT new it.rik.capstoneBE.autoparts.AutopartDTO(a.id, a.nome, a.codiceOe, a.descrizione, a.categoria, a.immagine, null, p.prezzo, r.ragioneSociale) " +
            "FROM Autopart a " +
            "JOIN a.prezzi p " +
            "JOIN p.venditore r" +
            " ORDER BY a.id DESC")
    List<AutopartDTO> findAllWithPriceAndReseller();


}



