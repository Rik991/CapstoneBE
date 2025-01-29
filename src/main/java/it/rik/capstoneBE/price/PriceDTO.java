package it.rik.capstoneBE.price;

import it.rik.capstoneBE.user.reseller.ResellerDTO;
import lombok.Data;

@Data
public class PriceDTO {
    private Long id;
    private double prezzo;
    private ResellerDTO venditore;
}