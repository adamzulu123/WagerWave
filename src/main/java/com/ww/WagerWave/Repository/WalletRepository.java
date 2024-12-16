package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Model.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalletRepository  extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findWalletByUser(MyUser user);
    List<Wallet> findByLastUpdateBefore(LocalDateTime dateTime);



    //Z TEGO NARAZIE NIE KORZYSTAM TO JAKO DODATEK!!!!
    //Query pozwala definiować zapytania do bazy danych za pomocą języka JPQL (Java Persistence Query Language).
    //dzięki czemu operujemy na stworzonych modelach a nie na tabeli
    @Transactional //wszystkie operacje jako jedna całosc (typowa transakcja w sql)
    @Modifying //oznaczenie ze zapytnie JPA wykonuje operacje modyfikujaca (delete, update)
    @Query("Update Wallet w SET w.balance = w.balance + :amount, w.lastUpdate = CURRENT_TIMESTAMP WHERE w.user = :user ")
    void updateWallet(MyUser user, BigDecimal amount);

}
