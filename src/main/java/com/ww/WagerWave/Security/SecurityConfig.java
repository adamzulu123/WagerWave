package com.ww.WagerWave.Security;

import com.ww.WagerWave.Services.UserServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/*
@Configuration - oznacza, że tak klasa to źrodlo definicji beanów oraz oznacza ze za pomoca
tej klasy będa odbywała configuracja springsecurity.
@EnableWebSecurity - włącza wsparcie dla konfiguracji bezpieczeństwa
 */

@Configuration
@EnableWebSecurity
@AllArgsConstructor //automatyczne generowanie kontruktora ktory zawiera wsztkie elementy klasy
public class SecurityConfig {

    @Autowired
    private final UserServices userServices;

    @Bean
    public UserDetailsService getUserDetailsService() {
        return userServices;
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServices);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

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
                    authorizeRequests.requestMatchers("/registration", "/register", "/css/**","/js/**" ).permitAll();
                    authorizeRequests.anyRequest().authenticated(); //każda inna musi być uwierzytelniony użytkownik
                })
                //konfiguracja ustawien logowania
                .formLogin(form -> form
                        .loginPage("/registration") //własna strona do logowania
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/Main", true) //po zalogowaniu, przekierowania na główna strone
                        .failureHandler(new CustomAuthenticationFailureHandler()) //dodanie handlera do obłsugi błedów logowania
                )
                .logout(config -> config
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/registration")) //po wylogownaniu powrót na strone do rejestracji

                .build(); //utworzenie i zwrocenie obiektu
    }
}
/*
.loginProcessingUrl("/login") - dzięki temu SpringSecurity wie na jaki adres URL powinien zostać przesłany
formularz logowania, w celu przetworzenia uwierzytelniania. Spring Sec przchwytuje to żadanie i przetwarza.
 */





