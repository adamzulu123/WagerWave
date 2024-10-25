package com.ww.WagerWave.Controller;


import com.ww.WagerWave.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }


}

