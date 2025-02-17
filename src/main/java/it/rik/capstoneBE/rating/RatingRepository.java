package it.rik.capstoneBE.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.reseller.id = :resellerId")
    Double calculateAverageRating(@Param("resellerId") Long resellerId);

    List<Rating> findByResellerId(Long resellerId);

}