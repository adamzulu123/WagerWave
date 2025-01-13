package com.ww.WagerWave.Services;


import com.ww.WagerWave.Model.*;
import com.ww.WagerWave.Repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BetsServices {

    private BetsRepository betsRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CouponRepository couponRepository;
    private WalletService walletService;


    public Optional<String> processSingleBets(BetRequest betRequest, MyUser user) {

        //pobieramy info o kwocie całej tych pojedynczych zakładów co chcemy obstawic
        BigDecimal totalStake = BigDecimal.ZERO;
        for (BetRequest.BetData betData : betRequest.getBets()) {
            totalStake = totalStake.add(BigDecimal.valueOf(betData.getStake()));
        }

        //jak nie ma kasy na te zakłady co użytkownic chce to zwracamy bład
        Wallet wallet = walletService.getWalletForUser(user);
        if(wallet.getBalance().compareTo(totalStake) < 0) {
            return Optional.of("Insufficient funds in wallet. You need at least " + totalStake + " to place your bet.");
        }

        //jak nie ma błędu odejmujemy siano
        walletService.placeBetWithYourFunds(user, totalStake);


        //formatter do dat
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for(BetRequest.BetData betData : betRequest.getBets()) {
            Event event = eventRepository.findById(betData.getEventID())
                    .orElseThrow(() -> new RuntimeException("Event not found: " + betData.getEventID()));

            Bet bet = Bet.builder()
                    .user(user)
                    .event(event)
                    .betType(BetType.valueOf(betData.getBetType()))
                    .odds(BigDecimal.valueOf(betData.getOdds()))
                    .stake(BigDecimal.valueOf(betData.getStake()))
                    .potentialWin(BigDecimal.valueOf(betData.getPotentialWin()))
                    .creationTime(LocalDateTime.now())
                    .endTime(LocalDateTime.parse(betData.getBetEndTime(), formatter))
                    .result(BetResult.PENDING)
                    .build();

            betsRepository.save(bet);
        }
        //nic nie zawracamy jak nie ma obłedu i dało sie zabetowac
        return Optional.empty();
    }

    public Optional<String> processComboBets(BetRequest betRequest, MyUser user) {
        //log.info();
        BetRequest.CouponData couponData = betRequest.getCouponData();
        BigDecimal totalStake = BigDecimal.valueOf(couponData.getStake());

        //sprawdzamy czy mamy siano na kupon i jak tak to stawiamy
        Wallet wallet = walletService.getWalletForUser(user);
        if(wallet.getBalance().compareTo(totalStake) < 0) {
            return Optional.of("Insufficient funds in wallet. You need at least " + totalStake + " to place your bet.");
        }
        walletService.placeBetWithYourFunds(user, totalStake);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Coupon coupon = Coupon.builder()
                .user(user)
                .odds(BigDecimal.valueOf(couponData.getOdds()))
                .stake(totalStake)
                .potentialWin(BigDecimal.valueOf(couponData.getPotentialWin()))
                .creationTime(LocalDateTime.now())
                .endTime(LocalDateTime.parse(couponData.getEndTime(), formatter))
                .result(CouponResult.PENDING)
                .build();

        coupon = couponRepository.save(coupon);

        for (BetRequest.BetData betData : betRequest.getBets()) {
            Event event = eventRepository.findById(betData.getEventID())
                    .orElseThrow(() -> new RuntimeException("Event not found: " + betData.getEventID()));

            Bet bet = Bet.builder()
                    .user(user)
                    .coupon(coupon)
                    .event(event)
                    .betType(BetType.valueOf(betData.getBetType()))
                    .odds(BigDecimal.valueOf(betData.getOdds()))
                    .stake(null)
                    .potentialWin(null)
                    .creationTime(LocalDateTime.now())
                    .endTime(LocalDateTime.parse(betData.getBetEndTime(), formatter))
                    .result(BetResult.PENDING)
                    .build();

            betsRepository.save(bet);
        }
        return Optional.empty(); //nic jak nie ma błedu customowego
    }
}

