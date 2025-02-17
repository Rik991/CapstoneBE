package it.rik.capstoneBE.rating;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> submitRating(@RequestBody RatingDTO ratingDTO) {
        Rating savedRating = ratingService.submitRating(ratingDTO);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping("/reseller/{resellerId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long resellerId) {
        Double avgRating = ratingService.getAverageRatingForReseller(resellerId);
        return ResponseEntity.ok(avgRating);
    }

    @GetMapping("reseller/{resellerId}")
    public ResponseEntity<List<Rating>> getRatingsForReseller(@PathVariable Long resellerId){
        List<Rating> ratings = ratingService.getRatingsForReseller(resellerId);
        return ResponseEntity.ok(ratings);
    }

}
