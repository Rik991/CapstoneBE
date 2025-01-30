package it.rik.capstoneBE.price;

import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "prices")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false)
    private double prezzo;

    @ManyToOne
    @JoinColumn(name = "ricambio_id", nullable = false)
    private Autopart autopart;

    @ManyToOne
    @JoinColumn(name = "venditore_id", nullable = false)
    private Reseller venditore;

}