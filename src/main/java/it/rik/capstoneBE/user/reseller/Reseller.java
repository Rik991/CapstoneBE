package it.rik.capstoneBE.user.reseller;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import it.rik.capstoneBE.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "resellers")
public class Reseller {
    @Id
    private Long id; // L'id verrà preso dall'utente associato

    @JsonUnwrapped
    @OneToOne
    @MapsId  // Indica che la chiave primaria è condivisa con la relazione
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String ragioneSociale;
    private String partitaIva;
    private String sitoWeb;


}