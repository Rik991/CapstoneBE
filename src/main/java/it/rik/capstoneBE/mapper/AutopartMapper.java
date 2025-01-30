package it.rik.capstoneBE.mapper;

import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.autoparts.AutopartResponseDTO;
import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.user.reseller.ResellerInfoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AutopartMapper {

    public AutopartResponseDTO toDTO(Autopart autopart, List<Price> prices) {
        return new AutopartResponseDTO(
                autopart.getId(),
                autopart.getNome(),
                autopart.getCodiceOe(),
                autopart.getDescrizione(),
                autopart.getCategoria(),
                autopart.getCondizione(),
                autopart.getImmagine(),
                toResellerInfoDTOs(prices)
        );
    }

    private List<ResellerInfoDTO> toResellerInfoDTOs(List<Price> prices) {
        return prices.stream()
                .map(price -> new ResellerInfoDTO(
                        price.getVenditore().getRagioneSociale(),
                        price.getVenditore().getSitoWeb(),
                        price.getPrezzo()))
                .collect(Collectors.toList());
    }
}