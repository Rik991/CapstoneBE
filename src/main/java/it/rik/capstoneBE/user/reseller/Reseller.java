package it.rik.capstoneBE.user.reseller;

import it.rik.capstoneBE.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "resellers")
public class Reseller {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String ragioneSociale;

    private String partitaIva;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}