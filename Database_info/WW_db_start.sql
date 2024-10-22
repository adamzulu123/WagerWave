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
INSERT INTO Users (first_name, last_name, password, email, age, gender, verified_status) 
VALUES ('Admin', 'Adminowski', 'hashed_password', 'admin@example.com', 30, 'M', 1);




