package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.vehicle.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutopartDetailsDTO {
        private Long id;
        private String nome;
        private String codiceOe;
        private String descrizione;
        private String categoria;
        private String condizione;
        private String immagine;
        private Set<Vehicle> veicoliCompatibili;
        private Double prezzo;

}