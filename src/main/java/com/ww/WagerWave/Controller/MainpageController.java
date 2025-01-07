package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Services.BasketballApiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class MainpageController {

    @Autowired
    private BasketballApiServices basketballApiServices;

    @GetMapping("/Main")
    public Mono<String> Mainpage() {
        String league = "12";
        String season = "2024-2025";
        String date = "2025-01-07";

        //wywolujemy metode która zapisuje eventy
        return basketballApiServices.getAndSaveGames(league, season, date)
                .thenReturn("Main");
    }
}
