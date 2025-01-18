package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.Bet;
import com.ww.WagerWave.Model.Coupon;
import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Services.BetsServices;
import com.ww.WagerWave.Services.UserServices;
import com.ww.WagerWave.Services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final BetsServices betsServices;
    private final UserServices userServices;
    private final WalletService walletService;

    @GetMapping("/History")
    public String showHistory(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());

        userOptional.ifPresent(user -> {
            model.addAttribute("user", user);
            Wallet wallet = walletService.getWalletForUser(user);
            model.addAttribute("wallet", wallet);

            /*
            List<Bet> bets = betsServices.getUserBets(user);
            model.addAttribute("bets", bets);

            List<Coupon> coupons = betsServices.getUserCoupons(user);
            model.addAttribute("coupons", coupons);
            */


        });


        return "History";
    }
}
