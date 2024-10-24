package com.ww.WagerWave.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
/*
@Configuration - oznacza, że tak klasa to źrodlo definicji beanów oraz oznacza ze za pomoca
tej klasy będa odbywała configuracja springsecurity.
@EnableWebSecurity - włącza wsparcie dla konfiguracji bezpieczeństwa
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            /*
            metoda authorizeHttpRequests - pozwala na konfigurowanie żądań na poziomie żądań HTTP

            @{/login} - tym w Thymeleaf w pliku html tworzymy te ścieżki url w aplikacji
            requestMatchers() - definujemy ścieżki URL brane po uwagę.
            /registration - to konkretna sciezka do mojej strony rejestracji
            /css/** i /js/** - to samo ale cieżki do plików w tych folderach. (to i tak tylko stylizacja)

            .permitAll() zezwala każemu nawet tym niezalogowanym na dostęp do strony rejestracji
            .anyRequest().authenticated() - to powoduje, że każdy inny zasbów/url/strona, która nie znajduję
            się wyżej w wyjątkach, wymaga aby użytkownik byl uwierzytelniony inaczej nie ma do niej dostępu.
             */

            return http
                    .authorizeHttpRequests(authorizeRequests ->{
                        authorizeRequests.requestMatchers("/registration", "/css/**","/js/**" ).permitAll();
                        authorizeRequests.anyRequest().authenticated(); //każda inna musi być uwierzytelniony użytkownik
                    })
                    //konfiguracja ustawien logowania
                    .formLogin(form -> form
                            .loginPage("/registration") //własna strona do logowania
                            .defaultSuccessUrl("/", true) //po zalogowaniu, przekierowania na główna strone
                    )
                    .logout(config -> config.logoutSuccessUrl("/")) //po wylogownaniu tez powrót na glówna
                    .build(); //utworzenie i zwrocenie obiektu
    }
}
