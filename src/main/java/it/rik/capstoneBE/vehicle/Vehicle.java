package it.rik.capstoneBE.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.rik.capstoneBE.autoparts.Autopart;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modello;

    @ElementCollection(fetch = FetchType.EAGER) // Caricamento immediato
    @CollectionTable(
            name = "vehicle_engine_types",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Column(name = "tipo_motore")
    @JsonIgnore
    private Set<String> tipiMotore;

    private String carrozzeria;

    @Column(nullable = false)
    private int inizioProduzione;

    private int fineProduzione;

    @ManyToMany(mappedBy = "veicoliCompatibili", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Autopart> ricambi;


}