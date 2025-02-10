package it.rik.capstoneBE.rating;

import lombok.Data;

@Data
public class RatingDTO {
    private int rating;
    private String comment;
    private Long resellerId;
    private Long userId;
}