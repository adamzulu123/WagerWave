package com.ww.WagerWave.Services;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Model.EventResult;
import com.ww.WagerWave.Model.EventStatus;
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
            return eventRepository.findByCategoryAndSubcategoryAndEventStartTimeAfterAndEventResult(
                    category, subcategory, today, EventResult.PENDING, sort);
        }
        return eventRepository.findByCategoryAndEventStartTimeAfterAndEventResult(
                category, today, EventResult.PENDING, sort);
    }

    //pobieramy wydarzenia do basic generowania stronki
    public List<Event> getUpcomingEvents() {
        LocalDateTime today = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Order.asc("eventStartTime"));
        return eventRepository.findAllByEventStartTimeAfterAndEventResult(today, EventResult.PENDING, sort);
    }

}
