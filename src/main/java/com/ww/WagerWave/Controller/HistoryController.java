package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.Bet;
import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Services.BetsServices;
import com.ww.WagerWave.Services.UserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final BetsServices betsServices;
    private final UserServices userServices;

    @GetMapping("/History")
    public String showHistory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        log.info("Zalogowany użytkownik: {}", userEmail);

        MyUser currentUser = userServices.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("Użytkownik nie istnieje w bazie: {}", userEmail);
                    return new IllegalStateException("Użytkownik nie znaleziony");
                });

        List<Bet> betsList = betsServices.getBetsByUser(currentUser);
        log.info("Pobrano {} zakładów dla użytkownika {}", betsList.size(), userEmail);

        model.addAttribute("bets", betsList);

        return "History";
    }
}
