package com.ww.WagerWave.Services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ww.WagerWave.Model.Event;
import com.ww.WagerWave.Model.EventResult;
import com.ww.WagerWave.Model.EventStatus;
import com.ww.WagerWave.Repository.EventRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
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


    //@PostConstruct powoduje ze wywołujemy to przy inicjacji tego obiektu, czyli zawsze podczas startu aplikacji
    //@Transactional zapewnia, że medoy będa działały w ramach jednej transakcji, jak cos pójdzie nie tak obie wycofane
    //@Async - metoda jest asychroniczna czyli bedzie działać w tle w osobnym wątku i nie blokować aplikacji
    /*
    @PostConstruct
    @Transactional
    @Async
    public void initialEventsUpdate(){
        String league = "12";
        String season = "2024-2025";
        LocalDate today = LocalDate.now();
        LocalDate tomorow = today.plusDays(3);
        //LocalDate twoDaysAfter = today.plusDays(4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = formatter.format(today);
        String tomorowDate = formatter.format(tomorow);
        //String twoDaysAfterDate = formatter.format(twoDaysAfter); //bez tego narazie bo baza nie wyrabia 2 dni w przód

        //teraz poobieramy dane z tych dni
        //.subscribe() w przeciwieństwie do block() nie blokuje głównego wątku, alee działa asynchronicznie.
        //dzięki temu zapisywanie i pobieranie odpowiedzi z api odbywa sie w tle podczas inicjalizaji obiektów.
        getAndSaveGames(league, season, todayDate).subscribe();
        getAndSaveGames(league, season, tomorowDate).subscribe();
    }
    */

    @Scheduled(initialDelay = 1800000, fixedRate = 3600000) // Po 30 minutach, a potem co godzinę
    public void scheduledEventUpdate(){
        String league = "12";
        String season = "2024-2025";
        LocalDate today = LocalDate.now();
        LocalDate tomorow = today.plusDays(3);
        //LocalDate twoDaysAfter = today.plusDays(4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDate = formatter.format(today);
        String tomorowDate = formatter.format(tomorow);
        //String twoDaysAfterDate = formatter.format(twoDaysAfter); //bez tego narazie bo baza nie wyrabia 2 dni w przód

        getAndSaveGames(league, season, todayDate).subscribe();
        getAndSaveGames(league, season, tomorowDate).subscribe();
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
                                    .ifPresentOrElse(existingEvent -> {
                                                EventResult newResult = determineEventResult(existingEvent,
                                                        gameElement.getAsJsonObject().getAsJsonObject("scores"));

                                                // Jeśli wynik się zmienił, aktualizujemy istniejący event
                                                if (newResult != existingEvent.getEventResult()) {
                                                    existingEvent.setEventResult(newResult);
                                                    existingEvent.setLastUpdated(LocalDateTime.now());
                                                    eventRepository.save(existingEvent);
                                                    log.info("Updated event: {}", existingEvent.getApiGameId());
                                                }
                                            },
                                            () -> {
                                                //zapisz event do bazy danych
                                                eventRepository.save(event);
                                                log.info("Saved new event: {}", event.getApiGameId());
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

        //odds - losowe generownaie narazie -zeby api nie przeciążyc
        BigDecimal oddsTeam1 = generateOdds();
        BigDecimal oddsTeam2 = generateOdds();
        BigDecimal oddsDraw = generateDrawOdds(oddsTeam1, oddsTeam2);

        //narazie domyslne wartosci, potem uzupełnie o dokładne
        return Event.builder()
                .apiGameId(gameId)
                .eventName(homeTeam.get("name").getAsString() + " vs " + awayTeam.get("name").getAsString())
                .category("Basketball")
                .subcategory("NBA")
                .team1(homeTeam.get("name").getAsString())
                .team2(awayTeam.get("name").getAsString())
                .oddsTeam1(oddsTeam1)
                .oddsTeam2(oddsTeam2)
                .oddsDraw(oddsDraw)
                .eventStartTime(eventStartTime)
                .eventEndTime(eventEndTime)
                .eventResult(EventResult.PENDING)
                .status(EventStatus.SCHEDULED)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    //metoda do sprawdzania wyników, porównaia czy wynik meczu jest już znany w api i czy nie został juz zaktualizowany w bazie
     private EventResult determineEventResult(Event existingEvent, JsonObject scores) {
         //jeśli wynik juz został wczesniej pobrany wiec nie trzeba tego ponownie aktualizować bazy
         //i zużywać jej ograniczone zasoby
         if (existingEvent.getEventResult() != EventResult.PENDING) {
             return existingEvent.getEventResult();
         }

         //pobierz wyniki
         JsonElement homeScoreElement = scores.getAsJsonObject("home").get("total");
         JsonElement awayScoreElement = scores.getAsJsonObject("away").get("total");

         //sprawdzamy czy element json wgl istnieje oraz czy element nie jest null w strukturze odpowiedzi api
         Integer homeScore = homeScoreElement != null && !homeScoreElement.isJsonNull()
                 ? homeScoreElement.getAsInt() : null;

         Integer awayScore = awayScoreElement != null && !awayScoreElement.isJsonNull()
                 ? awayScoreElement.getAsInt() : null;

         if (homeScore != null && awayScore != null) {
             if (homeScore > awayScore) {
                 return EventResult.TEAM_1;
             } else if (awayScore > homeScore) {
                 return EventResult.TEAM_2;
             } else {
                 return EventResult.DRAW;
             }
         }

         //jeśli nadal nie ma wyników
         return EventResult.PENDING;
     }

     //losowanie odds
     private BigDecimal generateOdds(){
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1.01, 5))
                .setScale(2, RoundingMode.HALF_UP);
     }
     private BigDecimal generateDrawOdds(BigDecimal odds1, BigDecimal odds2){
        return odds1.add(odds2).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
     }

}

















