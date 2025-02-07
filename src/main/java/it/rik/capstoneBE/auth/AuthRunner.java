package it.rik.capstoneBE.auth;

import it.rik.capstoneBE.user.Role;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Creazione dell'utente admin se non esiste
        Optional<User> adminUser = userService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest();
            adminRequest.setUsername("admin");
            adminRequest.setPassword("adminpwd");
            adminRequest.setEmail("admin@epicode.it");
            adminRequest.setName("Admin");
            adminRequest.setSurname("Supremo");
            adminRequest.setPhoneNumber("3351574563");
            userService.registerUser(adminRequest, null, Set.of(Role.ROLE_ADMIN));
        }

        // Creazione dell'utente user se non esiste
        Optional<User> normalUser = userService.findByUsername("user");
        if (normalUser.isEmpty()) {
            RegisterRequest userRequest = new RegisterRequest();
            userRequest.setUsername("user");
            userRequest.setPassword("userpwd");
            userRequest.setEmail("user@epicode.it");
            userRequest.setName("User");
            userRequest.setSurname("Schiavo");
            userRequest.setPhoneNumber("3391524563");
            userService.registerUser(userRequest, null, Set.of(Role.ROLE_USER));
        }

//        Optional<User> resellerUser = userService.findByUsername("reseller");
//        if (resellerUser.isEmpty()) {
//            RegisterRequest resellerRequest = new RegisterRequest();
//            resellerRequest.setUsername("reseller");
//            resellerRequest.setPassword("resellerpwd");
//            resellerRequest.setEmail("reseller@epicode.it");
//            resellerRequest.setName("Reseller");
//            resellerRequest.setSurname("Venditore");
//            resellerRequest.setRagioneSociale("Reseller SRL");
//            resellerRequest.setPartitaIva("12345678901");
//            userService.registerReseller(resellerRequest, null);
//        }
    }
}



