package com.ww.WagerWave.Repository;


import com.ww.WagerWave.Model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetsRepository extends JpaRepository<Bet, Integer> {
}
