package com.ww.WagerWave.Services;

import com.ww.WagerWave.Model.*;
import com.ww.WagerWave.Repository.BetsRepository;
import com.ww.WagerWave.Repository.CouponRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class BetResultUpdateService {

    private BetsRepository betsRepository;
    private CouponRepository couponRepository;
    private WalletService walletService;

    //lekko opóznione w stosunku do basketballAPI
    @Scheduled(initialDelay = 2000000, fixedRate = 3800000)
    public void checkAndUpdateBets(){
        log.info("Checking event results and updating bets...");

        LocalDateTime twoDaysBefore = LocalDateTime.now()
                .minusDays(2);

        List<Bet> bets = betsRepository.findByEndTimeGreaterThanEqual(twoDaysBefore);

        for (Bet bet : bets) {
            if(bet.getResult() == BetResult.PENDING){
                Event event = bet.getEvent();
                if (event.getEventResult() != EventResult.PENDING){
                    if (checkBet(bet, event)) {
                        bet.setResult(BetResult.WON);
                        MyUser user = bet.getUser();
                        if (user != null && bet.getCoupon() == null) {
                            //aktualizowanie portfella po wygranej - ale tylko jak to pojedynczy, bo kupony aktualizujemy potem
                            walletService.updateFundsAfterWin(user, bet.getPotentialWin());
                        }

                    }else {
                        bet.setResult(BetResult.LOST);
                    }
                    betsRepository.save(bet);
                }
            }
        }

        //no i teraz po zaktualizowaniu wszystkich betów, aktualizujemy też kupony
        updateCoupons(bets);
    }

    private boolean checkBet(Bet bet, Event event){
        switch (bet.getBetType()) {
            case TEAM_1 -> {
                return event.getEventResult() == EventResult.TEAM_1;
            }
            case TEAM_2 -> {
                return event.getEventResult() == EventResult.TEAM_2;
            }
            case DRAW -> {
                return event.getEventResult() == EventResult.DRAW;
            }
            default -> {
                return false;
            }
        }
    }

    //update wyników kuponów
    protected void updateCoupons(List<Bet> bets){
        // Grupowanie betów w zależności od kuponu
        Map<Integer, List<Bet>> betsGroupedByCoupon = bets.stream()
                .filter(bet -> bet.getCoupon() != null)
                .collect(Collectors.groupingBy(bet -> bet.getCoupon().getId()));

        //zbieramy indefikatorów kuponów któych bety mogły zostac zaktualizowane
        List<Integer> couponIds = new ArrayList<>(betsGroupedByCoupon.keySet());

        //wszystkie kuponu w jednym zapytaniu i potem tworzymy mapę kuponów z identyfikatorem jako kluczem!
        List<Coupon> coupons = couponRepository.findAllById(couponIds);
        Map<Integer, Coupon> couponMap = coupons.stream()
                .collect(Collectors.toMap(Coupon::getId, coupon -> coupon));

        //Iterujemy po grupach betów dla każdego kuponu
        for (Map.Entry<Integer, List<Bet>> entry : betsGroupedByCoupon.entrySet()) {
            List<Bet> betList = entry.getValue();
            Coupon coupon = couponMap.get(entry.getKey());

            if (coupon != null && coupon.getResult() == CouponResult.PENDING) {
                boolean allBetsWon = betList.stream().allMatch(bet -> bet.getResult() == BetResult.WON);
                boolean anyBetPending = betList.stream().anyMatch(bet -> bet.getResult() == BetResult.PENDING);

                if (allBetsWon) {
                    coupon.setResult(CouponResult.WON);
                    MyUser user = coupon.getUser();
                    if (user != null) {
                        //aktualizowanie wallet po wygranej kuponu
                        walletService.updateFundsAfterWin(user, coupon.getPotentialWin());
                    }

                } else if (anyBetPending) {
                    coupon.setResult(CouponResult.PENDING);
                } else {
                    coupon.setResult(CouponResult.LOST);
                }

                couponRepository.save(coupon);
            }
        }
    }




}
