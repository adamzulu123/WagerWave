package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Services.EventServices;
import com.ww.WagerWave.Services.UserServices;
import com.ww.WagerWave.Services.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class MainpageController {

    private EventServices eventServices;
    private UserServices userServices;
    private final WalletService walletService;

    @GetMapping("/Main")
    public String Mainpage(Model model, Principal principal) {
        if (principal == null) {
            //jeśli uzytkownik nie zalogowany - to tak na wszelki zrobione
            return "redirect:/registration";
        }
        List<Event> events = eventServices.getUpcomingEvents();
        model.addAttribute("events", events);

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());

        userOptional.ifPresent(user -> {
            model.addAttribute("user", user);
            Wallet wallet = walletService.getWalletForUser(user);
            model.addAttribute("wallet", wallet);
        });

        return "Main";
    }
}
