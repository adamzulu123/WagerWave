package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

//kontroler do obłsugi stronki z kontem klienta
@Controller
@AllArgsConstructor
public class AccountController {

    private final UserServices userServices;

    @GetMapping("Account")
    public String showAccount(Principal principal, Model model) {
        //pobieramy uzytkownika z bazy
        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
        /*
        na wszelki sprawdzany czy present , ale w sumie musi byc bo tylko po zalowaniu jest dostep
        do dalszej czesci kodu. dodajemy user do modelu z klucznem "user" dzieki czemu teraz
        Thymeleaf widzi nasz obiekt user, co pozwala wypisac dane użytkownik a
         */
        userOptional.ifPresent(user -> model.addAttribute("user", user));

        return "Account"; // Upewnij się, że ten widok istnieje
    }

}


