SET character_set_client='utf8';
SET character_set_connection='utf8';
SET character_set_database='utf8';
SET character_set_results='utf8';
SET character_set_server='utf8';

CREATE DATABASE IF NOT EXISTS supermarket_db;
USE supermarket_db;

CREATE TABLE IF NOT EXISTS users(
id INT AUTO_INCREMENT PRIMARY KEY,
email VARCHAR(255) UNIQUE,
user_password VARCHAR(255),
first_name VARCHAR(255),
last_name VARCHAR(255),
phone CHAR(13),
user_role enum("ROLE_CLIENT", "ROLE_ADMIN") DEFAULT "ROLE_CLIENT"
);


CREATE TABLE IF NOT EXISTS products(
id INT AUTO_INCREMENT PRIMARY KEY,
product_name VARCHAR(255),
trademark VARCHAR(255),
price DECIMAL(5, 2),
category enum("BAKERY", "FRUIT_VEGETABLES", "FROZEN_FOOD", "DAIRY_PRODUCTS", "MEAT", "ALCOHOL") NOT NULL,
size VARCHAR(255),
amount BIGINT,
product_status enum("ACTIVE", "ARCHIVED") NOT NULL DEFAULT "ACTIVE",
UNIQUE unique_index(product_name, trademark, size)
);


CREATE TABLE IF NOT EXISTS orders(
id INT AUTO_INCREMENT PRIMARY KEY,
user_id INT,
payment ENUM("CARD", "CASH"),
delivery ENUM("DELIVERY", "PICKUP"),
address VARCHAR(255),
order_status ENUM("ACCEPTED", "REJECTED", "CANCELLED", "DONE") DEFAULT "ACCEPTED", 
update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS order_products(
order_id INT,
product_id INT,
product_amount BIGINT DEFAULT 1,
FOREIGN KEY (order_id) REFERENCES orders (id),
FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT IGNORE INTO products(product_name, price, category, size, amount, trademark) VALUES
("Банан", 4.88, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Лимон", 7.90, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Огірок", 2.80, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Виноград кишмиш", 5.70, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Перець червоний", 7.80, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Томат", 4.00, "FRUIT_VEGETABLES", "100.0г", 1000, ""),
("Свинячий шашлик маринований", 18.40, "MEAT", "100.0г", 1000, ""),
("Куряче філе", 13.90, "MEAT", "100.0г", 1000, ""),
("Фарш свинячий", 14.90, "MEAT", "100.0г", 1000, ""),
("Куряча гомілка", 8.00, "MEAT", "100.0г", 1000, ""),
("Куряче стегно", 10.30, "MEAT", "100.0г", 1000, ""),
("Батон \"Нарізний\"", 20.49, "BAKERY", "500.0г", 1000, ""),
("Сирна паличка", 9.49, "BAKERY", "70.0г", 1000, ""),
("Батон \"Колосок\"", 18.59, "BAKERY", "450.0г", 1000, "Київхліб"),
("Багет французький", 14.49, "BAKERY", "500.0г", 1000, ""),
("Хліб \"Соборний\" подовий", 26.69, "BAKERY", "800.0г", 1000, "Фастівський ХК"),
("Молоко пастеризоване 1%", 34.89, "DAIRY_PRODUCTS", "870.0г", 1000, "\"Яготинське\""),
("Біфітойогурт натуральний питний 1,5%", 22.99, "DAIRY_PRODUCTS", "290.0г", 1000, "\"Активіа\""),
("Біфітойогурт полуниця-злаки 1,5%", 25.39, "DAIRY_PRODUCTS", "870.0г", 1000, "\"Активіа\""),
("Кефір 1%", 25.99, "DAIRY_PRODUCTS", "900.0г", 1000, "\"Повна Чаша\""),
("Ряжанка 4%", 21.49, "DAIRY_PRODUCTS", "450.0г", 1000, "\"Яготинська\""),
("Морозиво Maximuse брикет", 28.99, "FROZEN_FOOD", "90.0г", 1000, "\"Ласка\""),
("Морозиво Love&Berries", 26.49, "FROZEN_FOOD", "150.0г", 1000, "\"Три медведі\""),
("Морозиво \"Моржо\" пломбір", 103.00, "FROZEN_FOOD", "500.0г", 1000, "\"Три медведі\""),
("Пельмені \"Фірмові\"", 179.00, "FROZEN_FOOD", "800.0г", 1000, "\"Три медведі\""),
("Віскі", 599.00, "ALCOHOL", "0.5л", 1000, "Jack Daniels"),
("Горілка \"Класична\"", 119.00, "ALCOHOL", "0.5л", 1000, "\"Хлібний дар\""),
("Пиво \"1715\" світле", 68.99, "ALCOHOL", "2.3л", 1000, "\"Львівське\""),
("Вино ігристе Fragolino Bianco", 149.00, "ALCOHOL", "0.75л", 1000, "Donelli"),
("Пиво Blanc", 46.49, "ALCOHOL", "0.46л", 1000, "Kronenbourg"),
("Джин London Dry 41.2%", 799.00, "ALCOHOL", "0.7л", 1000, "Hayman's");
