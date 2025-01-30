package it.rik.capstoneBE.user.reseller;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ragioneSociale;

    private String partitaIva;

    private String sitoWeb;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}