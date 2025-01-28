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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

            // --- Pobieramy WSZYSTKIE zakłady i kupony (do bilansu) ---
            List<Bet> allBets = betsServices.getUserBets(user);
            List<Coupon> allCoupons = betsServices.getUserCoupons(user);

            // --- Sortujemy BETY malejąco po endTime (najnowsze pierwsze) ---
            allBets.sort(Comparator.comparing(Bet::getEndTime, Comparator.nullsLast(Comparator.reverseOrder())));
            // Możesz użyć powyższej linijki, aby obsłużyć ewentualne null-endTime.
            // Jeżeli wiesz, że endTime NIGDY nie jest nullem, wystarczy:
            // allBets.sort((b1,b2) -> b2.getEndTime().compareTo(b1.getEndTime()));

            // --- Sortujemy KUPONY malejąco po endTime ---
            allCoupons.sort(Comparator.comparing(Coupon::getEndTime, Comparator.nullsLast(Comparator.reverseOrder())));

            // --- Wyliczamy bilans w oparciu o WSZYSTKIE bety ---
            BigDecimal totalStakeAll = allBets.stream()
                    .map(Bet::getStake)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalWonAll = allBets.stream()
                    .filter(bet -> bet.getResult() == BetResult.WON)
                    .map(Bet::getPotentialWin)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal netProfitAll = totalWonAll.subtract(totalStakeAll);

            // --- Filtrowanie listy do wyświetlenia (tabela) ---
            List<Bet> filteredBets = allBets;
            List<Coupon> filteredCoupons = allCoupons;

            if (!"ALL".equalsIgnoreCase(filter)) {
                filteredBets = filteredBets.stream()
                        .filter(bet -> bet.getResult().name().equalsIgnoreCase(filter))
                        .collect(Collectors.toList());

                filteredCoupons = filteredCoupons.stream()
                        .filter(coupon -> coupon.getResult().name().equalsIgnoreCase(filter))
                        .collect(Collectors.toList());
            }

            // --- Dodaj do modelu przefiltrowane listy ---
            model.addAttribute("bets", filteredBets);
            model.addAttribute("coupons", filteredCoupons);
            model.addAttribute("filter", filter);

            // --- Dodaj do modelu bilans (z całej listy) ---
            model.addAttribute("totalStake", totalStakeAll);
            model.addAttribute("totalWon", totalWonAll);
            model.addAttribute("netProfit", netProfitAll);
        });

        return "History";
    }
}
