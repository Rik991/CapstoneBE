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
        Optional<User> adminUser = userService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest();
            adminRequest.setUsername("admin");
            adminRequest.setPassword("adminpwd");
            adminRequest.setEmail("admin@epicode.it");
            adminRequest.setName("Admin");
            adminRequest.setSurname("Supremo");
            adminRequest.setPhoneNumber("3351574563");
            userService.registerAdmin(adminRequest, null);
        }

        // Creazione dell'utente normale se non esiste
        Optional<User> normalUser = userService.findByUsername("user");
        if (normalUser.isEmpty()) {
            RegisterRequest userRequest = new RegisterRequest();
            userRequest.setUsername("user");
            userRequest.setPassword("userpwd");
            userRequest.setEmail("user@epicode.it");
            userRequest.setName("User");
            userRequest.setSurname("Schiavo");
            userRequest.setPhoneNumber("3391524563");
            userService.registerUser(userRequest, null);
        }

        // Creazione di 5 venditori (reseller) se non esistono già
        for (int i = 1; i <= 5; i++) {
            String username = "reseller" + i;
            Optional<User> resellerUser = userService.findByUsername(username);
            if (resellerUser.isEmpty()) {
                RegisterRequest resellerRequest = new RegisterRequest();
                resellerRequest.setUsername(username);
                resellerRequest.setPassword("resellerpwd" + i);
                resellerRequest.setEmail("reseller" + i + "@epicode.it");
                resellerRequest.setName("Reseller" + i);
                resellerRequest.setSurname("Venditore" + i);
                resellerRequest.setPhoneNumber("33515745" + i);

                // Impostiamo i campi specifici per il venditore
                resellerRequest.setRagioneSociale("Reseller SRL " + i);
                resellerRequest.setPartitaIva("12345678901" + i);
                resellerRequest.setSitoWeb("www.reseller" + i + ".it");

                userService.registerReseller(resellerRequest, null);
            }
        }
    }
}


