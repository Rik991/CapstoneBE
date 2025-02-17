package it.rik.capstoneBE.rating;

import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.reseller.Reseller;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


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
   @OnDelete(action = OnDeleteAction.CASCADE)
   private Reseller reseller;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
   private User user;
}