package com.ww.WagerWave.Repository;

import com.ww.WagerWave.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {
    //potem do historii transakcji bedzie mozna tutaj dodac jakies wyszukiwanie transakcji dla klienta itp
}
