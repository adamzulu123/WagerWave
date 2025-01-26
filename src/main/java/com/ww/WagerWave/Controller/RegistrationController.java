package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Repository.UserRepository;
import com.ww.WagerWave.Repository.WalletRepository;
import com.ww.WagerWave.Services.EmailSenderServices;
import com.ww.WagerWave.Services.PasswordServices;
import com.ww.WagerWave.Services.UserServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Controller
@AllArgsConstructor
public class RegistrationController {

    //wskrzykiwanie intancji klas UserService i interfejsu UserRepository
    //repo umozliwia komunikacje z baza przy pomocy operacju CRUD (Create, Read, Update, Delete)
    @Autowired
    private final UserServices userServices;

    private final WalletRepository walletRepository;

    @Autowired
    private final PasswordServices passwordServices;

    @Autowired
    private final EmailSenderServices emailSenderServices;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private View error;


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
        //model.addAttribute("user", user);

        if (!user.isAdult()){
           result.addError(new FieldError("user", "birthdate",
                   "You must be 18 to register"));

        }
         if(repo.existsByEmail(user.getEmail())){
            result.addError(new FieldError("user", "email",
                    "This email is already in use!"));
        }

        if (result.hasErrors()) {
            model.addAttribute("user", new MyUser()); //czyszczenie danych w obiekie user
            model.addAttribute("registrationStatus", "failed");
            return "registration";
        }

        //zakodowanie hasła przed wpisaniem do bazy danych
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        MyUser savedUser = repo.save(user);

        //dodanie domyślnego portfela dla użytkownika
        Wallet defaultWallet = Wallet.builder()
                .user(savedUser)
                .balance(BigDecimal.ZERO)
                .dailyLimit(BigDecimal.valueOf(200))
                .remainingLimit(BigDecimal.valueOf(200))
                .lastUpdate(LocalDateTime.now())
                .build();

        walletRepository.save(defaultWallet);

        model.addAttribute("registrationStatus", "success");
        return "registration";
    }

    //generowanie losowego tymczasowego hasła
    private String generateTemporaryPass(){
        return String.valueOf((int) (Math.random() * 1_000_000_000)).substring(0, 8);
    }

    @PostMapping("/forgot-password")
    public String sendTempPassword(@RequestParam String emailForgetPass,
                                   HttpSession session) {

        Optional<MyUser> userOptional = userServices.findByEmail(emailForgetPass);
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();

            String tempPassword = generateTemporaryPass();

            passwordServices.updatePassword(user, tempPassword);

            //wysyłanie maila z info o zmienionym hasle
            String subject = "Password Reset";
            String body = "Hi, \n\nYour temporary password is: " + tempPassword +
                    "\n\nPlease log in and change your password as soon as possible." +
                    "\n\nYour favorite WagerWave Team ;))))";

            try {
                emailSenderServices.sendEmail(emailForgetPass, subject, body);
                session.setAttribute("message", "Temporary password has been sent to your email.");
            } catch (Exception e) {
                System.err.println("Error while sending mail: " + e.getMessage());
                session.setAttribute("message", "Error while sending email: " + e.getMessage());
            }
        } else {
            session.setAttribute("message", "No account associated with this email.");
        }

        return "redirect:/login";
    }


    //to jednak nie jest potrzebne ale narazie zostawie - Spring security automatycznie/sam
    //zarządzi całym procesem juz samo to loginProcessingUrl("/login") - wystarczy

    /*
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<MyUser> userOptional = userServices.findByEmail(email);
        System.out.println(userOptional);

        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Użytkownik jest zalogowany, przekieruj na stronę główną
                return "redirect:/Main"; // lub inna strona główna
            } else {
                model.addAttribute("error", "Invalid password");
                System.out.println(model.getAttribute("error"));
            }
        } else {
            model.addAttribute("error", "User not found");
        }
        return "/Registration"; // Zwróć do widoku logowania w przypadku błędu
    }
     */


}


