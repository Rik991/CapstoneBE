package it.rik.capstoneBE.mapper;


import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartDTO;
import it.rik.capstoneBE.favourite.Favourite;
import it.rik.capstoneBE.favourite.FavouriteDTO;
import it.rik.capstoneBE.price.Prezzo;
import it.rik.capstoneBE.price.PrezzoInfo;
import it.rik.capstoneBE.rating.RatingRepository;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerInfo;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    @Autowired
    private RatingRepository ratingRepository;


    public AutopartDTO.Response mapToResponse(Autopart autopart) {
        AutopartDTO.Response response = new AutopartDTO.Response();
        response.setId(autopart.getId());
        response.setNome(autopart.getNome());
        response.setCodiceOe(autopart.getCodiceOe());
        response.setDescrizione(autopart.getDescrizione());
        response.setCategoria(autopart.getCategoria());
        response.setCondizione(autopart.getCondizione());
        response.setImmagine(autopart.getImmagine());

        response.setVeicoliCompatibili(autopart.getVeicoliCompatibili().stream()
                .map(this::mapVehicle)
                .toList());

        response.setPrezzi(autopart.getPrezzi().stream()
                .map(this::mapPrezzo)
                .toList());

        if (autopart.getReseller() != null) {
            response.setReseller(mapReseller(autopart.getReseller()));
            response.setRatingMedio(ratingRepository.calculateAverageRating(autopart.getReseller().getId()).orElse(0.0));
        } else {
            response.setReseller(null);
            response.setRatingMedio(0.0);
        }

        return response;
    }

    // Metodi di mapping per i DTO info
    public VehicleInfo mapVehicle(Vehicle vehicle) {
        VehicleInfo info = new VehicleInfo();
        info.setId(vehicle.getId());
        info.setMarca(vehicle.getMarca());
        info.setModello(vehicle.getModello());
        return info;
    }

    public PrezzoInfo mapPrezzo(Prezzo prezzo) {
        PrezzoInfo info = new PrezzoInfo();
        info.setImporto(prezzo.getImporto());
        info.setDataInserimento(prezzo.getDataInserimento());
        return info;
    }

    public ResellerInfo mapReseller(Reseller reseller) {
        ResellerInfo info = new ResellerInfo();
        info.setRagioneSociale(reseller.getRagioneSociale());
        info.setSitoWeb(reseller.getSitoWeb());
        return info;
    }

    //mapping DTO favoriti
    public FavouriteDTO matToFavouriteDTO(Favourite favourite){
        FavouriteDTO favouriteDTO = new FavouriteDTO();
        favouriteDTO.setAutopartId(favourite.getAutopart().getId());
        favouriteDTO.setUserId(favourite.getUser().getId());
        return favouriteDTO;
    }



}
