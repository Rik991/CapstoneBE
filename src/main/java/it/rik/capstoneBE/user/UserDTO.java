package it.rik.capstoneBE.user;


import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String avatar;
    private Set<Role> roles;
}