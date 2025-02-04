package it.rik.capstoneBE.favourite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.rik.capstoneBE.autoparts.Autopart;
import it.rik.capstoneBE.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "autopart_id")
    private Autopart autopart;





}