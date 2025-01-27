package it.rik.capstoneBE.user.dto;


import jakarta.validation.constraints.NotNull;

public record LoginDTO(

        @NotNull(message = "Username is required")
        String username,

        @NotNull(message = "Password is required")
        String password
) {
}
