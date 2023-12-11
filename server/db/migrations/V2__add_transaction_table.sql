CREATE TABLE `transaction` (
    transactionId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    name VARCHAR(255),
    date DATETIME,
    type VARCHAR(50),
    currency VARCHAR(25),
    value DOUBLE,
    category VARCHAR(100),
    description VARCHAR(250),
    paymentMethod VARCHAR(100) DEFAULT NULL,
    FOREIGN KEY (userId) REFERENCES `user`(userId)
);
