package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.vehicle.Vehicle;
import it.rik.capstoneBE.vehicle.VehicleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutopartDTO {

        private Long id;
        private String nome;
        private String codiceOe;
        private String descrizione;
        private String categoria;
        private Condizione condizione;
        private String immagine;

}