CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       age INT NOT NULL,
                       gender ENUM('M', 'F') NOT NULL,
                       verified_status TINYINT DEFAULT 0
);