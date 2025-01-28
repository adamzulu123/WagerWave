CREATE TABLE wallets (
    wallet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    balance DECIMAL(12, 2) DEFAULT 0.00 NOT NULL,
    daily_limit DECIMAL(12, 2) DEFAULT NULL,
    remaining_limit DECIMAL(12, 2) DEFAULT NULL,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id)
) ;

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    wallet_id INT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'BET_SPENT', 'BET_WON') NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id),
    INDEX idx_wallet_id (wallet_id)
) ;

CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    team_1 VARCHAR(100) NOT NULL,
    team_2 VARCHAR(100) NOT NULL,
    odds_team_1 DECIMAL(8, 2) NOT NULL,
    odds_team_2 DECIMAL(8, 2) NOT NULL,
    odds_draw DECIMAL(8, 2) DEFAULT NULL,
    event_start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    event_end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    event_result ENUM('PENDING', 'TEAM_1', 'TEAM_2', 'DRAW') DEFAULT 'PENDING' NOT NULL
);


CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    coupon_odds DECIMAL(8, 2) NOT NULL,
    coupon_stake DECIMAL(12, 2) NOT NULL,
    coupon_potential_win DECIMAL(12, 2) NOT NULL,
    coupon_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    coupon_end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    coupon_result ENUM('PENDING', 'WON', 'LOST') DEFAULT 'PENDING' NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    INDEX idx_user_id (user_id)
) ;

CREATE TABLE bets (
    bet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    coupon_id INT DEFAULT NULL,
    event_id INT NOT NULL,
    bet_type ENUM('TEAM_1', 'TEAM_2', 'DRAW') NOT NULL,
    bet_odds DECIMAL(8, 2) NOT NULL,
    bet_stake DECIMAL(12, 2) DEFAULT NULL,
    bet_potential_win DECIMAL(12, 2) DEFAULT NULL,
    bet_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    bet_end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    bet_result ENUM('PENDING', 'WON', 'LOST') DEFAULT 'PENDING' NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id)
) ;
