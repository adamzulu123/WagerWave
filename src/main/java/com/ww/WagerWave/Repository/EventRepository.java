package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByApiGameId(String apiGameId); //metoda do znajdowania meczu po apiGameId
}
