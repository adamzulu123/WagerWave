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

    /*
    ResponseEntity jest używane do zwracania to reprezentowania odpowiedzi HTTP serwera.
    W tym status (200 -OK, 400-BadRequest), nagłowke i odpowiedź.
    czyli np:
     ResponseEntity.ok().body(Map.of("message", "Bets placed successfully"))); :
            *ResponseEntity.ok() === status odpowidzi na 200
            *body() ==== komunikat w formacie JSON:
                {
                    "message": "Bets placed successfully"
                }
                dzieje się tak bo spring korzysta z: HttpMessageConverter (domyślnie)
                    wiec automatycznie serializuje obiekt Map do formatu JSON, bo
                    w nagłówkach odpowiedzi ustawione jest Accept: application/json

                    bo: (w naszym przypadku oczywiście)
                        Content-Type mówi serwerowi, że wysyłane dane są w formacie JSON.
                        Accept mówi serwerowi, że oczekujesz odpowiedzi w formacie JSON.
     */

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



