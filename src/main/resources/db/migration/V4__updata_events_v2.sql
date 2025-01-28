-- id z api do pobieranie na podstawie tego id potem odds
ALTER TABLE events
    ADD COLUMN api_game_id VARCHAR(50) NOT NULL;

-- status eventu
ALTER TABLE events
    ADD COLUMN status ENUM('SCHEDULED', 'PENDING', 'FINISHED', 'CANCELLED') DEFAULT 'SCHEDULED';

-- zeby sprawdzac do ile aktualizowac eventy te nie zakonczone
ALTER TABLE events
    ADD COLUMN last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

