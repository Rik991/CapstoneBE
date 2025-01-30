package it.rik.capstoneBE.price;

import it.rik.capstoneBE.user.reseller.ResellerDTO;
import lombok.Data;

@Data
public class PriceDTO {

    private double prezzo;
    private Long autopartId;
    private Long venditoreId;
}