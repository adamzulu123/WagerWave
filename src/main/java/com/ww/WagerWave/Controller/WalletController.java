package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Repository.WalletRepository;
import com.ww.WagerWave.Services.UserServices;
import com.ww.WagerWave.Services.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    private final UserServices userServices;

    private final WalletRepository walletRepository;

    @GetMapping("/depositPage")
    public String showDepositPage(Principal principal, Model model) {
        return "Deposit";
    }
    @GetMapping("/withdrawPage")
    public String showWithdrawPage(Principal principal, Model mode){
        return "Withdraw";
    }

    //jak cos to juz w html wymuszam ze kod blik ma miec 6 cyfr a kwota wieksza niz 5 zł
    @PostMapping("/depositFunds")
    public String depositFunds(BigDecimal amount, Principal principal, Model model) {
        try {
            Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
            if (userOptional.isPresent()) {
                MyUser user = userOptional.get();

                // Doładowanie środków
                walletService.depositFunds(user, amount);
                model.addAttribute("successMessage", "Funds deposited successfully.");
                System.out.println("Funds deposited successfully.");
            } else {
                model.addAttribute("errorMessage", "User not found.");
                return "Deposit";
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "Deposit";
    }

    @PostMapping("/withdrawFunds")
    public String withdrawFunds(BigDecimal amount, Principal principal, Model model) {
        try {
            Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
            if (userOptional.isPresent()) {
                MyUser user = userOptional.get();

                walletService.withdrawFunds(user, amount);
                model.addAttribute("successMessage", "Funds withdrawn successfully.");
                System.out.println("Funds deposited successfully.");
            } else {
                model.addAttribute("errorMessage", "User not found.");
                return "Withdraw";
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "Withdraw";
        }

        return "Withdraw";
    }

    @PostMapping("/setDailyLimit")
    public String setDailyLimit(@RequestParam("newLimit") BigDecimal newDailyLimit,
                                Principal principal, Model model) {

        try {
            Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
            if (userOptional.isPresent()) {
                MyUser user = userOptional.get();

                walletService.setDailyLimit(user, newDailyLimit);
                System.out.println("Limit successfully set.");

                Wallet wallet = walletService.getWalletForUser(user); //pobieramy wallet ze sprawdzonym limitem

                model.addAttribute("wallet", wallet);
                model.addAttribute("user", user);
            }
            else{
                System.out.println("User not found");
            }

        } catch (Exception e) {
            return "redirect:/registration";
        }
        return "Account";
    }



}
