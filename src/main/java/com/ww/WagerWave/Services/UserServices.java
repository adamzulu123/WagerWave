package com.ww.WagerWave.Services;

import com.ww.WagerWave.Model.User;
import com.ww.WagerWave.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


/*
@Service - uznacza ze ta klasa to kompoment serwisowy Stringa, czyli przez niego bedziemy wskrzykiwali
(dependency injection) instancje tej klasy do innych komponentów.
Oznacza to tylke ze ta klasa odpowiedzialna jest za logike, nie za Repository jak kontakt z baza,
czy Controller za prezentacje.

implements UserDetailsService - interfejs SpringSecurity pozwalajacy ładować uzytkownika, u nas na podstawie email

(do ogarniecia jutro)

 */

@Service
@AllArgsConstructor
public class UserServices implements UserDetailsService {

    //wstrzykujemy instancje naszego Repository, aby skorzystac z jego metod do zarzadzania baza
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user =userRepository.findByEmail(email); //zwracamy użytkownika na podstawie email

        /*
        isPresent() - sprawdza czy obiekt Optional nie jest pusty
         */
        if (user.isPresent()) {
            var userObj = user.get();
            return new org.springframework.security.core.userdetails.User(
                    userObj.getEmail(),
                    userObj.getPassword(),
                    new ArrayList<>() // Pusta lista uprawnień, dodaj uprawnienia, jeśli są
            );
        }
        else{
            throw new UsernameNotFoundException(email);
        }
    }
}
