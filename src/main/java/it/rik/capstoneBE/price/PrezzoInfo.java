package it.rik.capstoneBE.price;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrezzoInfo {
    private Double importo;
    private LocalDateTime dataInserimento;
}