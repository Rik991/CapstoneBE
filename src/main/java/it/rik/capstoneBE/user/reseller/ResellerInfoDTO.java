package it.rik.capstoneBE.user.reseller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class ResellerInfoDTO {
    private String ragioneSociale;
    private String sitoWeb;
    private double prezzo;
}
