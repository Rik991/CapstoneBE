package it.rik.capstoneBE.user;


import it.rik.capstoneBE.exceptions.BadRequestException;
import it.rik.capstoneBE.user.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"*"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public void getMe() {

    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody LoginDTO userLoginDto, BindingResult validation) {
        //controllo che i campi siano presenti
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage()).toString());
        //body -> username, password
        //validare username
        User user = userService.findByUsername(userLoginDto.username());
        //validare password, se lo user è nullo o se la password non corrisponde allora errore
        if ((user == null || userService.checkPassword(userLoginDto.password(), user.getPassword()))) {
            throw new BadRequestException("Username or password is wrong");
        }else {
            //generare token else Badrequest
        }
    }





}

