package it.rik.capstoneBE.price;

import it.rik.capstoneBE.autoparts.Autoparts;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;

    @Column(nullable = false)
    private double prezzo;

    @ManyToOne
    @JoinColumn(name = "ricambio_id", nullable = false)
    private Autoparts autoparts;

    @ManyToOne
    @JoinColumn(name = "venditore_id", nullable = false)
    private Reseller venditore;


}