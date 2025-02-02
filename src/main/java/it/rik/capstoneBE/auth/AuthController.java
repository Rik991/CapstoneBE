package it.rik.capstoneBE.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.rik.capstoneBE.user.Role;
import it.rik.capstoneBE.user.User;
import it.rik.capstoneBE.user.UserService;
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


    @PostMapping(path = "/register-user", consumes = {"multipart/form-data"})
    public ResponseEntity<User> registerUser(@RequestParam("appUser") String appUser,
                                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest registerRequest;

        try {
            registerRequest = objectMapper.readValue(appUser, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella conversione del JSON", e);
        }

        User registeredUser = userService.registerUser(registerRequest, avatar, Set.of(Role.ROLE_USER));
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }


    @PostMapping(path = "/register-reseller", consumes = {"multipart/form-data"})
    public ResponseEntity<User> registerReseller(@RequestParam("appUser") String appUser,
                                                 @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                                 @RequestParam("ragioneSociale") String ragioneSociale,
                                                 @RequestParam("partitaIva") String partitaIva,
                                                 @RequestParam("sitoWeb") String sitoWeb) {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest registerRequest;

        try {
            registerRequest = objectMapper.readValue(appUser, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Errore nella conversione del JSON", e);
        }

        registerRequest.setRagioneSociale(ragioneSociale);
        registerRequest.setPartitaIva(partitaIva);
        registerRequest.setSitoWeb(sitoWeb);

        User registeredReseller = userService.registerReseller(registerRequest, avatar);
        return new ResponseEntity<>(registeredReseller, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(authResponse);
    }
}
