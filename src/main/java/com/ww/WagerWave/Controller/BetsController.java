package com.ww.WagerWave.Controller;

import com.ww.WagerWave.Model.BetRequest;
import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Services.BetsServices;
import com.ww.WagerWave.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/bets")
public class BetsController {

    private BetsServices betsServices;
    private UserServices userServices;

    @PostMapping("/place")
    public ResponseEntity<Map<String, String>> placeBets (@RequestBody BetRequest betRequest, Principal principal) {

        Optional<MyUser> userOptional = userServices.findByEmail(principal.getName());
        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();


            if ("SINGLE".equalsIgnoreCase(betRequest.getType())) {
                Optional<String> errorMessage = betsServices.processSingleBets(betRequest, user);
                // Jeśli zwrócono komunikat o błędzie, zwracamy go , jak nie to info o sukcesie
                return errorMessage.map(s -> ResponseEntity.badRequest().body(Map.of("error", s)))
                        .orElseGet(() -> ResponseEntity.ok().body(Map.of("message", "Bets placed successfully")));


            } else if ("COMBO".equalsIgnoreCase(betRequest.getType())) {
                Optional<String> errorMessage = betsServices.processComboBets(betRequest, user);
                return errorMessage.map(s -> ResponseEntity.badRequest().body(Map.of("error", s)))
                        .orElseGet(() -> ResponseEntity.ok().body(Map.of("message", "Bets placed successfully")));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid bet type"));
            }

        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid user"));
    }
}



