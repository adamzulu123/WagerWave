# Struktura bazy danych

## 1. Tabela: `users`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `user_id`          | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator użytkownika.   |
| `email`            | VARCHAR(255)   | UNIQUE, NOT NULL                        | Adres e-mail użytkownika.             |
| `hashed_password`  | VARCHAR(255)   | NOT NULL                                | Zahaszowane hasło użytkownika.        |
| `first_name`       | VARCHAR(100)   | NOT NULL                                | Imię użytkownika.                     |
| `last_name`        | VARCHAR(100)   | NOT NULL                                | Nazwisko użytkownika.                 |
| `birth_date`       | DATE           | NOT NULL                                | Data urodzenia użytkownika.           |
| `account_creation` | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Data utworzenia konta.                |
| `verified`         | BIT            | DEFAULT 0, NOT NULL                     | Status weryfikacji.                   |

---

## 2. Tabela: `wallets`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `wallet_id`        | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator portfela.      |
| `user_id`          | INT            | UNIQUE, NOT NULL                        | Identyfikator użytkownika.             |
| `balance`          | DECIMAL(12, 2) | DEFAULT 0.00, NOT NULL                  | Saldo portfela.                       |
| `daily_limit`      | DECIMAL(12, 2) | DEFAULT NULL                            | Dzienny limit wydatków.               |
| `remaining_limit`  | DECIMAL(12, 2) | DEFAULT NULL                            | Pozostały limit wydatków.             |
| `last_update`      | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, NOT NULL | Czas ostatniej aktualizacji.           |

---

## 3. Tabela: `transactions`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `transaction_id`   | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator transakcji.    |
| `wallet_id`        | INT            | NOT NULL                                | Identyfikator portfela.               |
| `transaction_type` | ENUM           | ('DEPOSIT', 'WITHDRAWAL', 'BET_SPENT', 'BET_WON'), NOT NULL | Typ transakcji.                       |
| `amount`           | DECIMAL(12, 2) | NOT NULL                                | Kwota transakcji.                     |
| `transaction_time` | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas transakcji.                      |

---

## 4. Tabela: `events`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `event_id`         | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator wydarzenia.    |
| `event_name`       | VARCHAR(100)   | NOT NULL                                | Nazwa wydarzenia.                     |
| `category`         | VARCHAR(100)   | NOT NULL                                | Kategoria wydarzenia.                 |
| `team_1`          | VARCHAR(100)   | NOT NULL                                | Nazwa drużyny 1.                      |
| `team_2`          | VARCHAR(100)   | NOT NULL                                | Nazwa drużyny 2.                      |
| `odds_team_1`      | DECIMAL(8, 2)  | NOT NULL                                | Kurs na drużynę 1.                    |
| `odds_team_2`      | DECIMAL(8, 2)  | NOT NULL                                | Kurs na drużynę 2.                    |
| `odds_draw`        | DECIMAL(8, 2)  | DEFAULT NULL                            | Kurs na remis.                        |
| `event_start_time` | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas rozpoczęcia wydarzenia.          |
| `event_end_time`   | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas zakończenia wydarzenia.          |
| `event_result`     | ENUM           | ('PENDING', 'TEAM_1', 'TEAM_2', 'DRAW'), DEFAULT 'PENDING', NOT NULL | Wynik wydarzenia.                     |
| `api_game_id`|
|`status`|
|`last_updated`|
---

## 5. Tabela: `coupons`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `coupon_id`        | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator kuponu.        |
| `user_id`          | INT            | NOT NULL                                | Identyfikator użytkownika.             |
| `coupon_odds`      | DECIMAL(8, 2)  | NOT NULL                                | Łączny kurs kuponu.                   |
| `coupon_stake`     | DECIMAL(12, 2) | NOT NULL                                | Wysokość zakładu.                     |
| `coupon_potential_win`    | DECIMAL(12, 2) | NOT NULL                                | Potencjalna wygrana.                  |
| `coupon_creation`  | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas utworzenia kuponu.               |
| `coupon_end_time`  | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas zakończenia kuponu.              |
| `coupon_result`     | ENUM           | ('PENDING', 'WON', 'LOST'), DEFAULT 'PENDING', NOT NULL | Wynik kuponu.                         |

---

## 6. Tabela: `bets`

| Nazwa Kolumny     | Typ Danych     | Ograniczenia                            | Opis                                   |
|--------------------|----------------|-----------------------------------------|----------------------------------------|
| `bet_id`           | INT            | AUTO_INCREMENT, PRIMARY KEY             | Unikalny identyfikator zakładu.       |
| `user_id`          | INT            | NOT NULL                                | Identyfikator użytkownika.             |
| `coupon_id`        | INT            | DEFAULT NULL                            | Identyfikator kuponu (jeśli dotyczy). |
| `event_id`         | INT            | NOT NULL                                | Identyfikator wydarzenia.              |
| `bet_type`         | ENUM           | ('TEAM_1', 'TEAM_2', 'DRAW'), NOT NULL | Typ zakładu.                          |
| `bet_odds`         | DECIMAL(8, 2)  | NOT NULL                                | Kurs zakładu.                         |
| `bet_stake`        | DECIMAL(12, 2) | DEFAULT NULL                         | Wysokość zakładu (jeśli dotyczy).                   |
| `bet_potential_win`    | DECIMAL(12, 2) | DEFAULT NULL                            | Potencjalna wygrana (jeśli dotyczy).                  |
| `bet_creation`     | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas postawienia zakładu.             |
| `bet_end_time`     | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NOT NULL     | Czas zakończenia zakładu.             |
| `bet_result`       | ENUM           | ('PENDING', 'WON', 'LOST'), DEFAULT 'PENDING', NOT NULL | Wynik zakładu.                       |

---

## Relacje między tabelami

1. **Tabela `users` i `wallets`**
   - **Relacja:** 1 do 1
   - **Opis:** Każdy użytkownik (`users`) może mieć jeden portfel (`wallets`). Klucz obcy `user_id` w tabeli `wallets` odwołuje się do `user_id` w tabeli `users`.

2. **Tabela `wallets` i `transactions`**
   - **Relacja:** 1 do wielu
   - **Opis:** Każdy portfel (`wallets`) może mieć wiele transakcji (`transactions`). Klucz obcy `wallet_id` w tabeli `transactions` odwołuje się do `wallet_id` w tabeli `wallets`.

3. **Tabela `users` i `coupons`**
   - **Relacja:** 1 do wielu
   - **Opis:** Każdy użytkownik (`users`) może mieć wiele kuponów (`coupons`). Klucz obcy `user_id` w tabeli `coupons` odwołuje się do `user_id` w tabeli `users`.

4. **Tabela `coupons` i `bets`**
   - **Relacja:** 1 do wielu
   - **Opis:** Każdy kupon (`coupons`) może zawierać wiele zakładów (`bets`). Klucz obcy `coupon_id` w tabeli `bets` odwołuje się do `coupon_id` w tabeli `coupons`.

5. **Tabela `events` i `bets`**
   - **Relacja:** 1 do wielu
   - **Opis:** Każde wydarzenie (`events`) może mieć wiele zakładów (`bets`). Klucz obcy `event_id` w tabeli `bets` odwołuje się do `event_id` w tabeli `events`.

6. **Tabela `users` i `bets`**
   - **Relacja:** 1 do wielu
   - **Opis:** Każdy użytkownik (`users`) może postawić wiele zakładów (`bets`). Klucz obcy `user_id` w tabeli `bets` odwołuje się do `user_id` w tabeli `users`.

---

## Uwagi

- **Bezpieczeństwo haseł:** Hasło musi być szyfrowane.
- **Indeksowanie:** Użyto INDEX dla pól `transactions(wallet_id)`, `coupons(user_id)`, `bets(user_id)`, `bets(coupon_id)`.
- **Kupony, a zakłady:** Zakład może być postawiony oddzielnie, lub w kuponie. W kuponie może być dowolna liczba zakładów (również 1). Jeśli zakład należy do jakiegoś kuponu, to pola `stake` i `potential_win` w tablicy `bets` są wartości `NULL`, ponieważ stawka, a więc i potencjalna wygrana, jest ustawiana dla całego kuponu. Jeśli zakład jest samodzielny to wartość `NULL` ma pole z kluczem obcym `coupon_id`.
