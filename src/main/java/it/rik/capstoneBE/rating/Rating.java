package it.rik.capstoneBE.rating;

import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "ratings")
@Data
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
   private int rating;


   private String comment;

   @ManyToOne(optional = false)
   @JoinColumn(name = "reseller_id", nullable = false)
   private Reseller reseller;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
   private User user;
}