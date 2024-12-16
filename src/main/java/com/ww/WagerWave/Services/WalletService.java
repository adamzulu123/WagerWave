package com.ww.WagerWave.Services;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Transaction;
import com.ww.WagerWave.Model.TransactionType;
import com.ww.WagerWave.Model.Wallet;
import com.ww.WagerWave.Repository.TransactionsRepository;
import com.ww.WagerWave.Repository.WalletRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionsRepository transactionsRepository;


    //WAŻNE LAST UPDATE TRAKTUJE JAKO czas to resetowania limitów!!!!!!
    //sprawdzanie czy mineło juz 24h i mozemy znowy wrócic remaining limit
    @PostConstruct
    @Transactional
    public void updateAllWallletsLimits(){
        //znajdujemy wszystkie portfele których limity od 24h nie byly aktualizowane
        List<Wallet> outdatedWallets = walletRepository.findByLastUpdateBefore(LocalDateTime.now().minusDays(1));
        outdatedWallets.forEach(this::resetRemainingLimit); //dla każdego wywolujemy metode do aktualizacji limitu
        System.out.println("Updated wallets at startup: " + outdatedWallets.size() + " wallets updated.");
    }


    //ok
    public void resetRemainingLimit(Wallet wallet) {
        wallet.setRemainingLimit(wallet.getDailyLimit());
        wallet.setLastUpdate(LocalDateTime.now());
        walletRepository.save(wallet);
    }


    @Transactional
    public Wallet getWalletForUser(MyUser user) {
        return walletRepository.findWalletByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Transactional
    public void depositFunds(MyUser user, BigDecimal amount) {

        //pobieramy wallet uzytkownika - ze sprawdzonym limitem
        Wallet wallet = getWalletForUser(user);

        if(wallet.getRemainingLimit().compareTo(amount) < 0){
            throw new IllegalArgumentException("Deposit amount exceeds daily limit.");
        }

        //aktualizacja balance, limitu i daty transakcji
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setRemainingLimit(wallet.getRemainingLimit().subtract(amount));
        //wallet.setLastUpdate(LocalDateTime.now()); - nie aktualizujemy bo LastUpdate bedzie sluzyć do określenia
        //resetowanie licznika RemainingLimit, który okresla limit jaki uzytkownik moze w 24h wydać
        walletRepository.save(wallet);

        //musimy też dodać nową transakcję
        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .transactionType(TransactionType.DEPOSIT)
                .amount(amount)
                .transactionTime(LocalDateTime.now())
                .build();

        transactionsRepository.save(transaction);
    }

    @Transactional
    public void withdrawFunds(MyUser user, BigDecimal amount) {

        //pobieramy wallet uzytkownika - ze sprawdzonym limitem
        Wallet wallet = getWalletForUser(user);

        //aktualizacja - tutaj limit nie obowiazuje bo dotyczny to tylko wpłat
        wallet.setBalance(wallet.getBalance().subtract(amount));
        //wallet.setLastUpdate(LocalDateTime.now()); - nie aktualizujemy bo LastUpdate bedzie sluzyć do określenia
        //resetowanie licznika RemainingLimit, który okresla limit jaki uzytkownik moze w 24h wydać
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(amount)
                .transactionTime(LocalDateTime.now())
                .build();

        transactionsRepository.save(transaction);
    }

    @Transactional
    public void setDailyLimit(MyUser user, BigDecimal newDailyLimit){
        Wallet wallet = walletRepository.findWalletByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        //jeśli limit nowy został tak obnizony ze remaining bylby na minusie to wtedy ustawiamy go na 0 i tyle
        BigDecimal previouslySpent = wallet.getDailyLimit().subtract(wallet.getRemainingLimit());
        BigDecimal newRemainingLimit = newDailyLimit.subtract(previouslySpent);

        if(newRemainingLimit.compareTo(BigDecimal.ZERO) < 0){
            newRemainingLimit = BigDecimal.ZERO;
        }

        wallet.setRemainingLimit(newRemainingLimit);
        wallet.setDailyLimit(newDailyLimit);
        walletRepository.save(wallet);
    }
}
