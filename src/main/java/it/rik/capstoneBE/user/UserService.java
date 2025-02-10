package it.rik.capstoneBE.user;

import it.rik.capstoneBE.auth.AuthResponse;
import it.rik.capstoneBE.auth.RegisterRequest;
import it.rik.capstoneBE.auth.jwt.JwtTokenUtil;
import it.rik.capstoneBE.config.amazons3.FileStorageService;
import it.rik.capstoneBE.user.reseller.Reseller;
import it.rik.capstoneBE.user.reseller.ResellerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private FileStorageService fileStorageServiceAmazon;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResellerRepository resellerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    //metodo per creare uno user generico, verrà poi usato per register-user o register-reseller
    private User createUser (RegisterRequest registerRequest, MultipartFile avatar, Set<Role> roles){
        if (userRepository.existsByUsername(registerRequest.getUsername())){
            throw new EntityExistsException("Username già utilizzato");
        }
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);

        if(avatar != null && !avatar.isEmpty()){
            String fileName = fileStorageServiceAmazon.storeFile(avatar);
            user.setAvatar(fileName);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User registerUser (RegisterRequest registerRequest, MultipartFile avatar){
        return createUser(registerRequest, avatar, Set.of(Role.ROLE_USER));
    }

    public User registerAdmin (RegisterRequest registerRequest, MultipartFile avatar){
        return createUser(registerRequest, avatar, Set.of(Role.ROLE_ADMIN));
    }
    @Transactional
    public User registerReseller (RegisterRequest registerRequest, MultipartFile avatar){

        User user = createUser(registerRequest, avatar, Set.of(Role.ROLE_RESELLER));
        Reseller reseller = new Reseller();
        reseller.setUser(user);
        reseller.setRagioneSociale(registerRequest.getRagioneSociale());
        reseller.setPartitaIva(registerRequest.getPartitaIva());
        reseller.setSitoWeb(registerRequest.getSitoWeb());
        resellerRepository.save(reseller);

        return user;

    }





    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AuthResponse authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            User user = loadUserByUsername(username);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(jwtTokenUtil.generateToken(userDetails));
            authResponse.setUser(user);

            return authResponse;

        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));
    }

    public User updateUser(Long userId, RegisterRequest updateRequest, MultipartFile avatar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        BeanUtils.copyProperties(updateRequest, user, "id", "username", "password", "roles");

        if (avatar != null && !avatar.isEmpty()) {
            String fileName = fileStorageServiceAmazon.storeFile(avatar);
            user.setAvatar(fileName);
        }

        return userRepository.save(user);
    }

    @Transactional
    public Reseller updateReseller(Long userId, RegisterRequest updateRequest, MultipartFile avatar) {
        // Aggiorna i dati comuni dello User
        User user = updateUser(userId, updateRequest, avatar);

        // Aggiorna i dati specifici del Reseller
        Reseller reseller = resellerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Rivenditore non trovato"));
        reseller.setRagioneSociale(updateRequest.getRagioneSociale());
        reseller.setPartitaIva(updateRequest.getPartitaIva());
        reseller.setSitoWeb(updateRequest.getSitoWeb());
        return resellerRepository.save(reseller);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User non trovato"));
    }

}