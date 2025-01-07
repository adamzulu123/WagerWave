package com.ww.WagerWave.Services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Model.EventResult;
import com.ww.WagerWave.Model.EventStatus;
import com.ww.WagerWave.Repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BasketballApiServices {

    private final WebClient webClient;
    private final EventRepository eventRepository;

    //konstruktor polacznie z api
    public BasketballApiServices(WebClient.Builder webClientBuilder,
                                 @Value("${basketball.api.key}") String apiKey,
                                 @Value("${basketball.api.host}") String apiHost,
                                 EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.webClient = webClientBuilder
                .baseUrl("https://v1.basketball.api-sports.io")
                .defaultHeader("x-rapidapi-key", apiKey)
                .defaultHeader("x-rapidapi-host", apiHost)
                .build();
    }

    //jest to operacja asynchroniczna bo takie zapytanie moze zajac duzo czasu i zeby nie spowalniac działania aplikacji
    //metoda do pobierania gier i zapisywania ich do bazy
    public Mono<String> getAndSaveGames(String league, String season, String date) {
        return this.webClient.get()
                //tworzenie zapytania w postaci url do zapytania do api
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(String.class) //odbieramy odpowiedz jako string
                .flatMap(response -> {
                    try {
                        JsonArray games = JsonParser.parseString(response)
                                .getAsJsonObject()
                                .getAsJsonArray("response");

                        games.forEach(gameElement -> {
                            Event event = mapApiToEvent(gameElement.getAsJsonObject());

                            //sprawdzamy czy event istnieje w bazie i jesli nie to dodajemy
                            eventRepository.findByApiGameId(event.getApiGameId())
                                    .ifPresentOrElse(existingEvent -> {},
                                            () -> {
                                                //zapisz event do bazy danych
                                                eventRepository.save(event);
                                            });
                        });

                        return Mono.just("Games processed successfully");
                    } catch (Exception e) {
                        System.out.println("Error processing the API response: " + e.getMessage());
                        return Mono.error(e);
                    }
                });
    }

    //mapowanie odpowiedzi api na obiekt Eveny
    private Event mapApiToEvent(JsonObject game) {
        //game_id z API bo potem bedziemy pobieraly odds prawdopodobnie
        String gameId = game.get("id").getAsString();

        //pobieranie drużyn z api
        JsonObject homeTeam = game.getAsJsonObject("teams").getAsJsonObject("home");
        JsonObject awayTeam = game.getAsJsonObject("teams").getAsJsonObject("away");

        //konwertowanie daty z api na localDateTime aby do bazy pasowało: "2025-01-06T00:00:00+00:00"
        String dateString = game.get("date").getAsString();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime eventStartTime = offsetDateTime.toLocalDateTime();
        LocalDateTime eventEndTime = eventStartTime.plusHours(2);

        //narazie domyslne wartosci, potem uzupełnie o dokładne
        return Event.builder()
                .apiGameId(gameId)
                .eventName(homeTeam.get("name").getAsString() + " vs " + awayTeam.get("name").getAsString())
                .category("Basketball")
                .subcategory("NBA")
                .team1(homeTeam.get("name").getAsString())
                .team2(awayTeam.get("name").getAsString())
                .oddsTeam1(new BigDecimal("1.5"))
                .oddsTeam2(new BigDecimal("2.5"))
                .oddsDraw(new BigDecimal("3.0"))
                .eventStartTime(eventStartTime)
                .eventEndTime(eventEndTime)
                .eventResult(EventResult.PENDING)
                .status(EventStatus.SCHEDULED)
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}

















