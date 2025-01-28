package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.PriceDTO;
import it.rik.capstoneBE.vehicle.VehicleDTO;
import lombok.Data;

import java.util.Set;

@Data
public class AutopartsDTO {
    private Long id;
    private String nome;
    private String codiceOe;
    private String descrizione;
    private String categoria;
    private String immagine;
    private Set<VehicleDTO> veicoliCompatibili;
    private Set<PriceDTO> prezzi;
}