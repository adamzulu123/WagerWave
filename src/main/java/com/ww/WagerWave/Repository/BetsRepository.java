package com.ww.WagerWave.Repository;


import com.ww.WagerWave.Model.Bet;
import com.ww.WagerWave.Model.BetResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BetsRepository extends JpaRepository<Bet, Integer> {
    //pobieranie eventów poźniej niż dana data
    List<Bet> findByEndTimeGreaterThanEqual(LocalDateTime startOfDay);
}
