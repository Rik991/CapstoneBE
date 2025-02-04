package it.rik.capstoneBE.favourite;


import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartRepository;
import it.rik.capstoneBE.exceptions.BadRequestException;
import it.rik.capstoneBE.mapper.Mapper;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final AutopartRepository autopartRepository;
    private final Mapper mapper;


    @Transactional
    public FavouriteDTO addFavourite(Long autopartId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        Autopart autopart = autopartRepository.findById(autopartId).orElseThrow(() -> new EntityNotFoundException("Ricambio non trovato"));

        //controlliamo che non sia già presente
        favouriteRepository.findByUserAndAutopart(user, autopart).ifPresent(f -> {
            throw new BadRequestException("Ricambio già nei preferiti");
        });

        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setAutopart(autopart);

        Favourite savedFavourite = favouriteRepository.save(favourite);
        return mapper.matToFavouriteDTO(savedFavourite);
    }

    @Transactional
    public void removeFavourite(Long autopartId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        Autopart autopart = autopartRepository.findById(autopartId).orElseThrow(() -> new EntityNotFoundException("Ricambio non trovato"));

        Favourite favourite = favouriteRepository.findByUserAndAutopart(user, autopart).orElseThrow(() -> new EntityNotFoundException("Ricambio non trovato"));

        favouriteRepository.delete(favourite);
    }



    public List<FavouriteDTO> getFavouritesByUser(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        List<Favourite> favourites = favouriteRepository.findAllByUser(user);
        return favourites.stream().map(mapper::matToFavouriteDTO).collect(Collectors.toList());
    }


}
