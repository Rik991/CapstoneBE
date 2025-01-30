package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.user.reseller.ResellerInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutopartResponseDTO {
    private Long id;
    private String nome;
    private String codiceOe;
    private String descrizione;
    private String categoria;
    private Condizione condizione;
    private String immagine;
    private List<ResellerInfoDTO> resellerInfo;}