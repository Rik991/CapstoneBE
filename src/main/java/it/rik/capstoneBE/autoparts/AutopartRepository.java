package it.rik.capstoneBE.autoparts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface AutopartRepository extends JpaRepository<Autopart, Long> {


    @EntityGraph(attributePaths = {"veicoliCompatibili", "prezzi", "reseller"})
    Page<Autopart> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"veicoliCompatibili", "prezzi", "reseller"})
    @Query("SELECT a FROM Autopart a WHERE a.id = :id")
    Optional<Autopart> findByIdWithDetails(@Param("id") Long id);

    Page<Autopart> findByVeicoliCompatibiliId(Long vehicleId, Pageable pageable);
    Page<Autopart> findByPrezziImportoBetween(Double minPrice, Double maxPrice, Pageable pageable);

//    @Query("SELECT a FROM Autopart a JOIN a.veicoliCompatibili v WHERE v.id = :vehicleId")
//    Page<Autopart> findByVehicle(@Param("vehicleId") Long vehicleId, Pageable pageable);
//
//    @Query("SELECT a FROM Autopart a WHERE a.prezzi IN (SELECT p FROM Prezzo p WHERE p.importo BETWEEN :min AND :max)")
//    Page<Autopart> findByPriceRange(@Param("min") Double min, @Param("max") Double max, Pageable pageable);


}


