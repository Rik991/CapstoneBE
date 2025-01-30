package it.rik.capstoneBE.user.reseller;

import it.rik.capstoneBE.user.UserDTO;
import lombok.Data;

@Data
public class ResellerDTO {
    private Long id;
    private String ragioneSociale;
    private String partitaIva;
    private String sitoWeb;
    private UserDTO user;
}