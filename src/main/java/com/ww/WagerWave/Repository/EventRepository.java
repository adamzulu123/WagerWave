package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByApiGameId(String apiGameId); //metoda do znajdowania meczu po apiGameId

    //pobiramy mecze z danej kategorii i subkategorii ale tylko dzisiajsze i przyszłe - bo na strone główna tyle nas interesuje
    List<Event> findByCategoryAndSubcategoryAndEventStartTimeAfter(String category, String subcategory, LocalDateTime eventStartTime, Sort sort);

    //category: ALL - strona główna
    List<Event> findByCategoryAndEventStartTimeAfter(String category, LocalDateTime eventStartTime, Sort sort);
}


