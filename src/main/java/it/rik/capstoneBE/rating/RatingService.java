package it.rik.capstoneBE.rating;


import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserRepository;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Rating submitRating(RatingDTO ratingDTO) {
        // Recupera il reseller tramite l'id
        Reseller reseller = resellerRepository.findById(ratingDTO.getResellerId())
                .orElseThrow(() -> new EntityNotFoundException("Reseller non trovato con id: " + ratingDTO.getResellerId()));

        // Recupera l'utente tramite l'id
        User user = userRepository.findById(ratingDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con id: " + ratingDTO.getUserId()));

        // Crea l'entity Rating e popola i campi
        Rating rating = new Rating();
        rating.setRating(ratingDTO.getRating());
        rating.setComment(ratingDTO.getComment());
        rating.setReseller(reseller);
        rating.setUser(user);

        return ratingRepository.save(rating);
    }

    @Transactional(readOnly = true)
    public Double getAverageRatingForReseller(Long resellerId) {
        Double avg = ratingRepository.calculateAverageRating(resellerId);
        return (avg != null) ? avg : 0.0;
    }
}
