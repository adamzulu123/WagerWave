CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    passwd VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    birth_date DATE NOT NULL,
    account_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
) ;

CREATE TABLE wallets (
    wallet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    balance DECIMAL(12, 2) DEFAULT 0.00 NOT NULL,
    daily_limit DECIMAL(12, 2) DEFAULT NULL,
    remaining_limit DECIMAL(12, 2) DEFAULT NULL,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ;

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    wallet_id INT NOT NULL,
    transaction_type ENUM('deposit', 'withdrawal', 'bet_spent', 'bet_won') NOT NULL,
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
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    event_result ENUM('pending', 'team_1', 'team_2', 'draw') DEFAULT 'pending' NOT NULL
);


CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    odds DECIMAL(8, 2) NOT NULL,
    stake DECIMAL(12, 2) NOT NULL,
    potential_win DECIMAL(12, 2) NOT NULL,
    coupon_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    end_coupon_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    coupon_result ENUM('pending', 'won', 'lost') DEFAULT 'pending' NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_id (user_id)
) ;

CREATE TABLE bets (
    bet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    coupon_id INT DEFAULT NULL,
    event_id INT NOT NULL,
    bet_type ENUM('team_1', 'team_2', 'draw') NOT NULL,
    odds DECIMAL(8, 2) NOT NULL,
    stake DECIMAL(12, 2) DEFAULT NULL,
    potential_win DECIMAL(12, 2) DEFAULT NULL,
    bet_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_bet_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    bet_result ENUM('pending', 'won', 'lost') DEFAULT 'pending' NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id)
) ;
