package com.ww.WagerWave.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/*
@Entity wskazuje, że ta klasa jest encja (reprezentacja tabeli w bazie danych), dzięki temu hibernate wie,
że obiekty tej klasy będą przechowywane w bazie dancyh.
@Table(name = "Users") - okresla do jakiej tabeli w bazie danych będzie mapowana nasza klasa.
Domyślnie ta 2 adnotacja nie jest podana i wtedy zostanie stworzona tabela o takiej nazwie jak nazwa klasy.
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "Users")
public class MyUser {
    /*
    Określnie sposobu generacji primary key (AUTO-INCREMENT) oraz @Id wskazuja na klucz główny.
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name ="email", nullable = false, unique = true)
    private String email;
    @Column(name ="password", nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate; // Użycie LocalDate zamiast Date

    @Column(name = "verified_status", columnDefinition = "BIT")
    private Boolean verified;

    //wymagany przez jpa i hibernate
    public MyUser() {
        this.createdAt = LocalDateTime.now();
    }

}
