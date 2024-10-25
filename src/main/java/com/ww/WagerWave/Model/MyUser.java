package com.ww.WagerWave.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "Users")
@Builder
@AllArgsConstructor
public class MyUser {
    /*
    Określnie sposobu generacji primary key (AUTO-INCREMENT) oraz @Id wskazuja na klucz główny.
    */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "first_name", nullable = false)
    private String name;
    @Column(name = "last_name", nullable = false)
    private String last_name;
    @Column(name ="email", nullable = false)
    private String email;
    @Column(name ="password", nullable = false)
    private String password;
    @Column(name ="created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(nullable = false, length = 1)
    @Pattern(regexp = "M|F", message = "Gender must be 'M' or 'F'")
    private String gender;
    @Column(name = "verified_status")
    private Boolean verified;

    //wymagany przez jpa i hibernate
    public MyUser() {
    }

}
