package com.ww.WagerWave.Security;

import com.ww.WagerWave.Services.UserServices;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/*
@Configuration - oznacza, że tak klasa to źrodlo definicji beanów oraz oznacza ze za pomoca
tej klasy będa odbywała configuracja springsecurity.
@EnableWebSecurity - włącza wsparcie dla konfiguracji bezpieczeństwa
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServices userServices;

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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

        // Tworzymy handler do usuwania plików cookie przy wylogowywaniu
        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
                new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)
        );

        // Tworzymy domyślnego SecurityContextLogoutHandler
        //SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        //tego jednak nie trzeba dodawać bo on jest domyślnie używany jako jeden handlerow, jesli korzystamy z domyślnego
        //systemy wylogowywania od spring security - dlatego przy usunieciu konta z niego korzystam !!!
        //on domyślnie usuwa sesje uzytkownika - czysci dane sesji i kontekst bezpieczeństwa


        return http
                .authorizeHttpRequests(authorizeRequests ->{
                    authorizeRequests.requestMatchers("/registration", "/register","/images/**", "/css/**","/js/**", "/forgot-password").permitAll();
                    authorizeRequests.anyRequest().authenticated(); //każda inna musi być uwierzytelniony użytkownik
                })
                //konfiguracja ustawien logowania
                .formLogin(form -> form
                        .loginPage("/registration") //własna strona do logowania
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/Main", true) //po zalogowaniu, przekierowania na główna strone
                        .failureHandler(new CustomAuthenticationFailureHandler()) //dodanie handlera do obłsugi błedów logowania
                )
                //domyślne wylogowywanie
                .logout(config -> config
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/registration") //po wylogownaniu powrót na strone do rejestracji
                        .addLogoutHandler(clearSiteData))
                .sessionManagement(session -> session
                        .maximumSessions(-1)  //-1 brak limitu ilości sesji
                )
                .csrf(AbstractHttpConfigurer::disable)
                .build(); //utworzenie i zwrocenie obiektu
    }
}
/*
.loginProcessingUrl("/login") - dzięki temu SpringSecurity wie na jaki adres URL powinien zostać przesłany
formularz logowania, w celu przetworzenia uwierzytelniania. Spring Sec przchwytuje to żadanie i przetwarza.
 */





