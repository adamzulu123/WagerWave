package com.ww.WagerWave.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainpageController {

    @GetMapping("/Main")
    public String Mainpage() {
        return "Main";
    }

}
