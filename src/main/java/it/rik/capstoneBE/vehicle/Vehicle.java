package it.rik.capstoneBE.vehicle;

import it.rik.capstoneBE.autoparts.Autoparts;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modello;

    @Column(nullable = false)
    private String tipoMotore;

    private String carrozzeria;

    @Column(nullable = false)
    private int inizioProduzione;

    private int fineProduzione;

    @ManyToMany(mappedBy = "veicoliCompatibili")
    private Set<Autoparts> ricambi;

}