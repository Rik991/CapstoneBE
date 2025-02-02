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

    // AutopartRepository.java
    Page<Autopart> findByResellerId(Long resellerId, Pageable pageable);

    @EntityGraph(attributePaths = {"veicoliCompatibili", "prezzi", "reseller"})
    Page<Autopart> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"veicoliCompatibili", "prezzi", "reseller"})
    @Query("SELECT a FROM Autopart a WHERE a.id = :id")
    Optional<Autopart> findByIdWithDetails(@Param("id") Long id);

    Page<Autopart> findByVeicoliCompatibiliId(Long vehicleId, Pageable pageable);
    Page<Autopart> findByPrezziImportoBetween(Double minPrice, Double maxPrice, Pageable pageable);




    @Query("SELECT DISTINCT a FROM Autopart a " +
            "LEFT JOIN a.veicoliCompatibili v " +
            "LEFT JOIN a.prezzi p " +
            "WHERE (:codiceOe IS NULL OR a.codiceOe LIKE %:codiceOe%) " +
            "  AND (:categoria IS NULL OR a.categoria = :categoria) " +
            "  AND (:marca IS NULL OR v.marca = :marca) " +
            "  AND (:modello IS NULL OR v.modello = :modello) " +
            "  AND (:minPrezzo IS NULL OR p.importo >= :minPrezzo) " +
            "  AND (:maxPrezzo IS NULL OR p.importo <= :maxPrezzo) " +
            "  AND (:condizione IS NULL OR a.condizione = :condizione) " +
            "  AND (:search IS NULL OR (" +
            "    LOWER(a.nome) LIKE %:search% " +
            "    OR LOWER(a.descrizione) LIKE %:search% " +
            "    OR LOWER(a.descrizione) LIKE %:searchWords% " +
            "  ))")
    Page<Autopart> search(
            @Param("codiceOe") String codiceOe,
            @Param("categoria") String categoria,
            @Param("marca") String marca,
            @Param("modello") String modello,
            @Param("minPrezzo") Double minPrezzo,
            @Param("maxPrezzo") Double maxPrezzo,
            @Param("condizione") Condizione condizione,
            @Param("search") String search,
            @Param("searchWords") String searchWords,
            Pageable pageable
    );




}


