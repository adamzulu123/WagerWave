package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.*;
import com.ww.WagerWave.Services.BetsServices;
import com.ww.WagerWave.Services.UserServices;
import com.ww.WagerWave.Services.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

    private final BetsServices betsServices;
    private final UserServices userServices;
    private final WalletService walletService;

    @GetMapping("/History")
    public String showHistory(@RequestParam(value = "filter", required = false, defaultValue = "ALL") String filter,
                              Model model,
                              Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());

        userOptional.ifPresent(user -> {
            model.addAttribute("user", user);
            Wallet wallet = walletService.getWalletForUser(user);
            model.addAttribute("wallet", wallet);

            // Pobieramy zakłady i kupony
            List<Bet> bets = betsServices.getUserBets(user);
            List<Coupon> coupons = betsServices.getUserCoupons(user);

            // --- Obsługa filtra (jeśli chcesz zachować filtr) ---
            if (!"ALL".equalsIgnoreCase(filter)) {
                bets = bets.stream()
                        .filter(bet -> bet.getResult().name().equalsIgnoreCase(filter))
                        .toList();
                coupons = coupons.stream()
                        .filter(coupon -> coupon.getResult().name().equalsIgnoreCase(filter))
                        .toList();
            }
            model.addAttribute("bets", bets);
            model.addAttribute("coupons", coupons);
            model.addAttribute("filter", filter);

            // --- Wyliczmy prosty bilans ---
            // Suma stawek
            BigDecimal totalStake = bets.stream()
                    .map(Bet::getStake)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Suma wygranych (tylko te bety z wynikiem WON)
            BigDecimal totalWon = bets.stream()
                    .filter(bet -> bet.getResult() == BetResult.WON) // Upewnij się, że w enumie jest WON
                    .map(Bet::getPotentialWin)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Różnica między wygraną a postawioną kwotą
            BigDecimal netProfit = totalWon.subtract(totalStake);

            // Wkładamy do modelu, by wyświetlić w widoku
            model.addAttribute("totalStake", totalStake);
            model.addAttribute("totalWon", totalWon);
            model.addAttribute("netProfit", netProfit);
        });

        return "History";
    }
}
