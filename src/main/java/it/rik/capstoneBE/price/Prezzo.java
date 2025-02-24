package it.rik.capstoneBE.price;

import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "prezzi")
@Data
public class Prezzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double importo;

    private LocalDateTime dataInserimento = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autopart_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Autopart autopart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reseller_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reseller reseller;
}