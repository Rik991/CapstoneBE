package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "autoparts")
public class Autoparts {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codiceOe;

    private String descrizione;

    @Column(nullable = false)
    private String categoria;

    private String immagine;

    @ManyToMany
    @JoinTable(
            name = "vehicle_parts",
            joinColumns = @JoinColumn(name = "ricambio_id"),
            inverseJoinColumns = @JoinColumn(name = "veicolo_id")
    )
    private Set<Vehicle> veicoliCompatibili;

    @OneToMany(mappedBy = "autoparts", cascade = CascadeType.ALL)
    private Set<Price> prezzi;
}