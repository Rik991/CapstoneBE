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

    public User registerUser(RegisterRequest registerRequest, MultipartFile avatar, Set<Role> roles) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("Username già in uso");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);

        if (avatar != null && !avatar.isEmpty()) {
            String fileName = fileStorageServiceAmazon.storeFile(avatar);
            user.setAvatar(fileName);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User registerReseller(RegisterRequest registerRequest, MultipartFile avatar) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("Username già in uso");
        }

        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);

        if (avatar != null && !avatar.isEmpty()) {
            String fileName = fileStorageServiceAmazon.storeFile(avatar);
            user.setAvatar(fileName);
        }

        user.setRoles(Set.of(Role.ROLE_RESELLER));
        User savedUser = userRepository.save(user);

        Reseller reseller = new Reseller();
        reseller.setRagioneSociale(registerRequest.getRagioneSociale());
        reseller.setPartitaIva(registerRequest.getPartitaIva());
        reseller.setSitoWeb(registerRequest.getSitoWeb());
        reseller.setUser(savedUser);
        resellerRepository.save(reseller);

        return savedUser;
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
}