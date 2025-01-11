package com.ww.WagerWave.Services;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServices {
    private EventRepository eventRepository;

    //pobieramy mecze z danej kategorii i podkategorii, chyba ze pod kategorii nie ma to z całej kategorii
    public List<Event> getEventsByCategoryAndSubcategory(String category, String subcategory) {
        LocalDateTime today = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Order.asc("eventStartTime")); //sortwanie aby najnowsze events były u góry

        if (subcategory != null && !subcategory.isEmpty() && !subcategory.equals("ALL")) {
            return eventRepository.findByCategoryAndSubcategoryAndEventStartTimeAfter(category, subcategory, today, sort);
        }
        return eventRepository.findByCategoryAndEventStartTimeAfter(category, today, sort);
    }

}
