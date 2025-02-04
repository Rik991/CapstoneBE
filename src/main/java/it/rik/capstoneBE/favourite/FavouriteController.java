package it.rik.capstoneBE.favourite;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @PostMapping("/{autopartId}")
    public ResponseEntity<FavouriteDTO> addFavourite (@PathVariable Long autopartId, @AuthenticationPrincipal UserDetails userDetails){

        FavouriteDTO favouriteDto = favouriteService.addFavourite(autopartId, userDetails.getUsername());
        return ResponseEntity.ok(favouriteDto);
    }

    @DeleteMapping("/{autopartId}")
    public ResponseEntity<Void> removeFavourite (@PathVariable Long autopartId, @AuthenticationPrincipal UserDetails userDetails){

        favouriteService.removeFavourite(autopartId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FavouriteDTO>> getAllByUser (@AuthenticationPrincipal UserDetails userDetails){

      List<FavouriteDTO> favourites = favouriteService.getFavouritesByUser(userDetails.getUsername());
        return ResponseEntity.ok(favourites);
    }


}
