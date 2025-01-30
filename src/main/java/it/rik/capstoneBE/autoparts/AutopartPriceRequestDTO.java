// AutopartPriceRequestDTO.java
package it.rik.capstoneBE.autoparts;

import lombok.Data;

import java.util.Set;

@Data
public class AutopartPriceRequestDTO {
    private String nome;
    private String codiceOe;
    private String descrizione;
    private String categoria;
    private Condizione condizione;
    private String immagine;
    private double prezzo;
    private Long venditoreId;
    private Set<Long> veicoliCompatibiliIds;
}