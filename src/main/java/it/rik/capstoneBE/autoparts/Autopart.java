package it.rik.capstoneBE.autoparts;

import it.rik.capstoneBE.price.Prezzo;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "autoparts")
@Data
public class Autopart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String codiceOe;

    private String descrizione;
    private String categoria;

    @Enumerated(EnumType.STRING)
    private Condizione condizione;

    private String immagine;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "autopart_vehicle",
            joinColumns = @JoinColumn(name = "autopart_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private Set<Vehicle> veicoliCompatibili = new HashSet<>();

    @OneToMany(mappedBy = "autopart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prezzo> prezzi = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reseller_id")
    private Reseller reseller;
}