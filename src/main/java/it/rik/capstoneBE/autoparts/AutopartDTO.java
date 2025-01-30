package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AutopartDTO {

        private Long id;
        private String nome;
        private String codiceOe;
        private String descrizione;
        private String categoria;
        private String condizione;
        private String immagine;
        private Set<VehicleDTO> veicoliCompatibili;
        private Double prezzo;
        private String ragioneSociale;
        private String sitoWeb;
}