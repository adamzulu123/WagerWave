package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Services.EventServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainpageController {

    @Autowired
    private EventServices eventServices;

    @GetMapping("/Main")
    public String Mainpage(Model model) {
        List<Event> events = eventServices.getUpcomingEvents();
        model.addAttribute("events", events);

        return "Main";
    }
}
