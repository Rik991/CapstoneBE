package it.rik.capstoneBE.autoparts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.rik.capstoneBE.price.Price;
import it.rik.capstoneBE.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "autoparts")
public class Autopart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codiceOe;

    private String descrizione;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private Condizione condizione;

    private String immagine;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "vehicle_parts",
            joinColumns = @JoinColumn(name = "ricambio_id"),
            inverseJoinColumns = @JoinColumn(name = "veicolo_id")
    )
    private Set<Vehicle> veicoliCompatibili;


    @OneToMany(mappedBy = "autopart", cascade = CascadeType.ALL)
    private Set<Price> prezzi;
}