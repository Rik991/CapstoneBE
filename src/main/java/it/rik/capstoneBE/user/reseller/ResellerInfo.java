package it.rik.capstoneBE.user.reseller;

import lombok.Data;


@Data
public class ResellerInfo {
    private Long id;
    private String ragioneSociale;
    private String sitoWeb;
    private Double ratingMedio;
}

