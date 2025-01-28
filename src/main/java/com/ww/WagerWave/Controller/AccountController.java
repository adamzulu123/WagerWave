package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Services.PasswordServices;
import com.ww.WagerWave.Services.UserServices;
import com.ww.WagerWave.Services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

//kontroler do obłsugi stronki z kontem klienta
@Controller
@AllArgsConstructor
public class AccountController {

    private final UserServices userServices;
    @Autowired
    private final PasswordServices passwordServices;

    private final WalletService walletService;


    @GetMapping("Account")
    public String showAccount(Principal principal, Model model) {
        if (principal == null) {
            // Jeśli użytkownik nie jest zalogowany (brak sesji)
            return "redirect:/registration"; // Przekierowanie do strony logowania
        }
        //pobieramy uzytkownika z bazy
        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
        /*
        na wszelki sprawdzany czy present , ale w sumie musi byc bo tylko po zalowaniu jest dostep
        do dalszej czesci kodu. dodajemy user do modelu z klucznem "user" dzieki czemu teraz
        Thymeleaf widzi nasz obiekt user, co pozwala wypisac dane użytkownik a
         */
        userOptional.ifPresent(user -> {
            model.addAttribute("user", user);

            Wallet wallet = walletService.getWalletForUser(user); //pobieramy wallet sprawdzony przez timer!

            model.addAttribute("wallet", wallet);
        });

        return "Account"; // Upewnij się, że ten widok istnieje
    }




    //Spring Security przechowuje szczegóły zalogowanego użytkownika w kontekście bezpieczeństwa (tzw. SecurityContext)
    //i te szczgóły (dane) są potem dostępne dzięki obiektowi principal
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Principal principal,
                                 Model model) {

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            model.addAttribute("user", user);

            //teraz do szablonu musimy tez wallet dodawac
            Wallet wallet = walletService.getWalletForUser(user);
            model.addAttribute("wallet", wallet);


            if (!passwordServices.verifyPassword(user, currentPassword)) {
                model.addAttribute("errorMessage", "Original password does not match");
                System.out.println("Original password does not match");
                return "Account";
            }

            if (newPassword == null || newPassword.isEmpty()) {
                model.addAttribute("errorMessage", "New password cannot be empty");
                System.out.println("New password cannot be empty");
                return "Account";
            }

            passwordServices.updatePassword(user, newPassword);
            model.addAttribute("successMessage", "Password changed successfully");
            System.out.println("Password changed successfully");

        } else {
            model.addAttribute("errorMessage", "User not found");
            System.out.println("User not found");
        }

        return "Account";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String password,
                                Principal principal,
                                Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            model.addAttribute("user", user);

            //musimy wallet dodac
            Wallet wallet = walletService.getWalletForUser(user);
            model.addAttribute("wallet", wallet);

            if (!passwordServices.verifyPassword(user, password)) {
                model.addAttribute("errorMessage", "Original password does not match");
                return "Account";
            }

            //najpierw jego portfel usuwamy
            walletService.deleteWallet(user);
            // Usuwamy użytkownika z bazy danych
            userServices.deleteUser(user.getEmail());

            /*
            tutaj musimy tego handlera wywołac aby po usunieciu konta wyczyścic/zakonczyc sesje, usunac kontekst
            bezpieczeństwa - czyli zeby usunac dane z pamieci serwera, bez tego wywalamy go tylko z bazy danych
             */
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(request, response, null);

            // Przekierowanie na stronę główną lub rejestracji
            return "redirect:/registration";
        } else {
            model.addAttribute("errorMessage", "User not found");
            System.out.println("User not found");
        }

        return "Account";
    }





}


