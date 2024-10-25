package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
@Repository - mówi ze jest to interface jest repozytorium czyli interfejsem odpowiedzilna za komunikacje z baza
extends JpaRepository<MyUser, Integer> - ten interfejs zawiera metody do komunikacji z baza danych
        np. save, findById, findAll, delete itp
UserRepository to jest interfejs - jednak Spring automatycznie generuje jego implementacje podczas odpalania aplikacji,
co pozwala na korzystanie z jego metod jak w przypadku zwykłej klasy.


Metoda findByEmail() - dodana przeze mnie metoda pozwala na znalezienie uzytkownika w bazie
na postawie jego adresu email, co jest używane do logowania (bo logujemy sie mailem)

Optional<MyUser> - ozn, że może zawierac obiekt użytkownka lub byc pusty (jak nie ma go w bazie)
 */

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {
    Optional<MyUser> findByEmail(String username);

}
