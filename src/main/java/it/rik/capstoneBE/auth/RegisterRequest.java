package it.rik.capstoneBE.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;

    // Campi specifici del rivenditore
    private String ragioneSociale;
    private String partitaIva;

}
