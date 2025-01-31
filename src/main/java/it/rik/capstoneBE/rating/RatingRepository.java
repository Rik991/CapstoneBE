package it.rik.capstoneBE.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT AVG(r.voto) FROM Rating r WHERE r.reseller.id = :resellerId")
    Optional<Double> calculateAverageRating(Long resellerId);
}