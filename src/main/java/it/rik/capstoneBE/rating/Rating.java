package it.rik.capstoneBE.rating;

import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reseller reseller;

    private Integer voto;

    private String commento;

    private LocalDateTime dataCreazione = LocalDateTime.now();
}