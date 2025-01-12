package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Model.EventResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByApiGameId(String apiGameId); //metoda do znajdowania meczu po apiGameId

    //pobiramy mecze z danej kategorii i subkategorii ale tylko dzisiajsze i przyszłe oraz w stanie PENDING - czyli zakład nie zakończony
    List<Event> findByCategoryAndSubcategoryAndEventStartTimeAfterAndEventResult(String category,
                                                                                 String subcategory,
                                                                                 LocalDateTime eventStartTime,
                                                                                 EventResult eventResult,
                                                                                 Sort sort);

    //category: ALL - strona główna
    List<Event> findByCategoryAndEventStartTimeAfterAndEventResult(String category,
                                                                   LocalDateTime eventStartTime,
                                                                   EventResult eventResult,
                                                                   Sort sort);

    //wszystkie wydarzenia - do ładowania strony głownej przed wybraniem kategorii
    List<Event> findAllByEventStartTimeAfterAndEventResult(LocalDateTime dateTime,
                                                           EventResult eventResult,
                                                           Sort sort);

}


