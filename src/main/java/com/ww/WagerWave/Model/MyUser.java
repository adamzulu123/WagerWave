package com.ww.WagerWave.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.Date;

/*
@Entity wskazuje, że ta klasa jest encja (reprezentacja tabeli w bazie danych), dzięki temu hibernate wie,
że obiekty tej klasy będą przechowywane w bazie dancyh.
@Table(name = "Users") - okresla do jakiej tabeli w bazie danych będzie mapowana nasza klasa.
Domyślnie ta 2 adnotacja nie jest podana i wtedy zostanie stworzona tabela o takiej nazwie jak nazwa klasy.
 */
@Entity
@Table(name = "Users")
@Builder
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

    public MyUser(int id, String name, String last_name, String email, String password, Date createdAt, int age, String gender, Boolean verified) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.age = age;
        this.gender = gender;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getAge() {
        return age;
    }

    public @Pattern(regexp = "M|F", message = "Gender must be 'M' or 'F'") String getGender() {
        return gender;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(@Pattern(regexp = "M|F", message = "Gender must be 'M' or 'F'") String gender) {
        this.gender = gender;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
