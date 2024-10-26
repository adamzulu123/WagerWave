package com.ww.WagerWave.Controller;


import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Repository.UserRepository;
import com.ww.WagerWave.Services.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    //wskrzykiwanie intancji klas UserService i interfejsu UserRepository
    //repo umozliwia komunikacje z baza przy pomocy operacju CRUD (Create, Read, Update, Delete)
    @Autowired
    private final UserServices userServices;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //@GetMapping("/registration")
    //public String registration(){
    //    return "registration";
    //}

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        // tworzymy obiekt MyUser oraz dodajemy go do modelu, aby można było skorzystac z jego danych w widoku
        MyUser user = new MyUser();
        model.addAttribute("user", user);
        return "registration";
    }
    /*
    @Valid @ModelAttribute("user") MyUser user - dane przesłane do formularza są przypisane do
    obiektu user, a @Valid uruchamia walidacje danych

    BindingResult result - przechowuje wyniki walidacji
     */

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") MyUser user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "registration";
        }

        //zakodowanie hasła przed wpisaniem do bazy danych
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Zapisz użytkownika w bazie danych
        repo.save(user);
        return "redirect:/login";
    }
}


