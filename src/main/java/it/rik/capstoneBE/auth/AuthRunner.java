package it.rik.capstoneBE.auth;

import com.github.javafaker.Faker;
import it.rik.capstoneBE.autoparts.AutopartDTO;
import it.rik.capstoneBE.autoparts.AutopartService;
import it.rik.capstoneBE.autoparts.Condizione;
import it.rik.capstoneBE.user.Role;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private AutopartService autopartService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<User> adminUser = userService.findByUsername("admin1");
        if(adminUser.isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest();
            adminRequest.setUsername("admin1");
            adminRequest.setPassword("adminpwd");
            adminRequest.setEmail("admin@info.it");
            adminRequest.setName("AdminPro");
            adminRequest.setSurname("Maestro");
            adminRequest.setPhoneNumber("3351574573");
            userService.registerAdmin(adminRequest, null);
        }

        // Creazione dell'utente normale se non esiste
//        Optional<User> normalUser = userService.findByUsername("user");
//        if (normalUser.isEmpty()) {
//            RegisterRequest userRequest = new RegisterRequest();
//            userRequest.setUsername("user");
//            userRequest.setPassword("userpwd");
//            userRequest.setEmail("user@epicode.it");
//            userRequest.setName("User");
//            userRequest.setSurname("Schiavo");
//            userRequest.setPhoneNumber("3391524563");
//            userService.registerUser(userRequest, null);
//        }

        // Creazione di 5 venditori (reseller) se non esistono già
//        for (int i = 1; i <= 5; i++) {
//            String username = "reseller" + i;
//            Optional<User> resellerUser = userService.findByUsername(username);
//            if (resellerUser.isEmpty()) {
//                RegisterRequest resellerRequest = new RegisterRequest();
//                resellerRequest.setUsername(username);
//                resellerRequest.setPassword("resellerpwd" + i);
//                resellerRequest.setEmail("reseller" + i + "@epicode.it");
//                resellerRequest.setName("Reseller" + i);
//                resellerRequest.setSurname("Venditore" + i);
//                resellerRequest.setPhoneNumber("33515745" + i);
//
//                // Impostiamo i campi specifici per il venditore
//                resellerRequest.setRagioneSociale("Reseller SRL " + i);
//                resellerRequest.setPartitaIva("12345678901" + i);
//                resellerRequest.setSitoWeb("www.reseller" + i + ".it");
//
//                userService.registerReseller(resellerRequest, null);
//            }
//        }
        // --- Generazione di 15 ricambi (autoparts) random per ogni reseller ---
//        Faker faker = new Faker();
//
//        // Recupera tutti i reseller (la tabella reseller dovrebbe contenere solo i reseller)
//        List<Reseller> resellers = resellerRepository.findAll();
//        for (Reseller reseller : resellers) {
//            // Supponiamo che il reseller sia associato a un User (con username)
//            String username = reseller.getUser().getUsername();
//            // Genera 15 autopart per questo reseller
//            for (int j = 0; j < 15; j++) {
//                AutopartDTO.Request request = new AutopartDTO.Request();
//
//                // Generazione di dati casuali
//                // Puoi definire un array di nomi di ricambi per maggiore coerenza
//                String[] possibiliNomi = {"Filtro Olio", "Filtro Aria", "Pastiglie Freno", "Dischi Freno", "Cinghia di Distribuzione", "Pompa Acqua", "Alternatore", "Radiatore", "Batteria", "Amortizzatore"};
//                String nome = possibiliNomi[faker.number().numberBetween(0, possibiliNomi.length)];
//
//                String codiceOe = faker.number().digits(8);
//                String descrizione = faker.lorem().sentence(10);
//
//                String[] categorie = {"Filtro", "Freni", "Motore", "Sospensioni", "Illuminazione", "Elettronica"};
//                String categoria = categorie[faker.number().numberBetween(0, categorie.length)];
//
//                // Imposta la condizione in modo casuale: supponiamo che l'enum Condizione abbia i valori NUOVO e USATO
//                Condizione condizione = faker.bool().bool() ? Condizione.NUOVO : Condizione.USATO;
//
//                double prezzo = faker.number().randomDouble(2, 50, 500);
//
//                // Genera un set casuale di veicoli (i veicoli sono 421 in totale)
//                Set<Long> veicoliIds = new HashSet<>();
//                int numVeicoli = faker.number().numberBetween(1, 3); // assegna 1 o 2 veicoli per ogni ricambio
//                while (veicoliIds.size() < numVeicoli) {
//                    // Assumiamo che gli id veicolo vadano da 1 a 421
//                    long idVeicolo = faker.number().numberBetween(1, 422);
//                    veicoliIds.add(idVeicolo);
//                }
//
//                // Popola il DTO di richiesta (nota: l'immagine la lasciamo null)
//                request.setNome(nome);
//                request.setCodiceOe(codiceOe);
//                request.setDescrizione(descrizione);
//                request.setCategoria(categoria);
//                request.setCondizione(condizione);
//                request.setPrezzo(prezzo);
//                request.setVeicoliIds(veicoliIds);
//                request.setImmagine(null);
//
//                // Chiama il servizio per creare l'autopart per questo reseller
//                try {
//                    autopartService.createAutopart(request, username);
//                    System.out.println("Autopart creato per " + username + ": " + nome);
//                } catch (Exception e) {
//                    System.err.println("Errore durante la creazione dell'autopart per " + username + ": " + e.getMessage());
//                }
//            }
//        }

    }
}


