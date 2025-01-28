package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Services.EventServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//restController - bo zwracamy dane w formacie json do dynamicznego generowania events na podstawie wybranej kategorii
//korzystamy z tego w aplikacjach restFul API gdzie nie generujemy widoków tylkoz wracamy dane w postaci xml lub json,
//u nas jest tego mix ale tutaj akurat lepiej dynamicznie pobierac a nie ciagle generowac szablony
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class EventsController {
    @Autowired
    private final EventServices eventServices;

    @GetMapping("/events")
    public List<Event> getEvents(@RequestParam String category, @RequestParam(required = false) String subcategory) {
        return eventServices.getEventsByCategoryAndSubcategory(category, subcategory);
    }

}
