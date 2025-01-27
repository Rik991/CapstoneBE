package it.rik.capstoneBE.user;


import it.rik.capstoneBE.exceptions.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEconder;

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByUsername(String username) throws ElementNotFoundException   {
        return userRepository.findOneByUsername(username).orElse(null);
    }

    public User findByEmail(String email) throws ElementNotFoundException {
        return userRepository.findOneByEmail(email).orElse(null);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEconder.matches(rawPassword, encodedPassword);
    }
}
