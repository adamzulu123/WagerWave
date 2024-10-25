package com.ww.WagerWave.Controller;


import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Repository.UserRepository;
import com.ww.WagerWave.Services.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class RegistrationController {

    @Autowired
    private final UserServices userServices;

    @Autowired
    private UserRepository repo;


    //@GetMapping("/registration")
    //public String registration(){
    //    return "registration";
    //}

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        MyUser user = new MyUser();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") MyUser user,
                           BindingResult result,
                           Model model) {
        // Logowanie danych użytkownika przed zapisem
        System.out.println("Dane użytkownika: " + user);

        if (result.hasErrors()) {
            return "registration";
        }

        // Zapisz użytkownika w bazie danych
        repo.save(user);
        return "redirect:/login";
    }





}


