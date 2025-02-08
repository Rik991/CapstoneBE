package it.rik.capstoneBE.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.user.Role;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
import it.rik.capstoneBE.user.reseller.Reseller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ObjectMapper objectMapper;


    // Metodo ausiliario per convertire la stringa JSON in RegisterRequest
    private RegisterRequest parseRegisterRequest(String appUser) {
        try {
            return objectMapper.readValue(appUser, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella conversione del JSON", e);
        }
    }

    @PostMapping(path = "/register-user", consumes = {"multipart/form-data"})
    public ResponseEntity<User> registerUser(@RequestParam("appUser") String appUser,
                                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        RegisterRequest request = parseRegisterRequest(appUser);
        User registeredUser = userService.registerUser(request, avatar);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping(path = "/register-reseller", consumes = {"multipart/form-data"})
    public ResponseEntity<User> registerReseller(@RequestParam("appUser") String appUser,
                                                 @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                                 @RequestParam("ragioneSociale") String ragioneSociale,
                                                 @RequestParam("partitaIva") String partitaIva,
                                                 @RequestParam("sitoWeb") String sitoWeb) {
        RegisterRequest request = parseRegisterRequest(appUser);
        // Impostiamo i campi specifici del rivenditore
        request.setRagioneSociale(ragioneSociale);
        request.setPartitaIva(partitaIva);
        request.setSitoWeb(sitoWeb);

        User registeredReseller = userService.registerReseller(request, avatar);
        return new ResponseEntity<>(registeredReseller, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping(path = "/update-reseller/{userId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Reseller> updateReseller(@PathVariable Long userId,
                                                   @RequestParam("appUser") String appUser,
                                                   @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest updateRequest;

        try {
            updateRequest = objectMapper.readValue(appUser, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella conversione del JSON", e);
        }

        Reseller updatedReseller = userService.updateReseller(userId, updateRequest, avatar);
        return ResponseEntity.ok(updatedReseller);
    }
}
