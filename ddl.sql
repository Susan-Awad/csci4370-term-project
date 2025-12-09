-- Create database if it does not already exist
CREATE DATABASE IF NOT EXISTS budgetTracker;

-- Switch to database
USE budgetTracker;

-- Users table 
create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);


-- Categories table 
CREATE TABLE categories (
    category_id    INT AUTO_INCREMENT PRIMARY KEY,
    category_name  VARCHAR(100) NOT NULL,
    category_type  ENUM('INCOME', 'EXPENSE') NOT NULL,
    category_color VARCHAR(20),
    user_id        INT NOT NULL,

    CONSTRAINT fk_categories_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Transactions table 
CREATE TABLE transactions (
    transaction_id          INT AUTO_INCREMENT PRIMARY KEY,
    transaction_date        DATE NOT NULL,
    transaction_amount      DECIMAL(10, 2) NOT NULL,
    transaction_description VARCHAR(255),
    user_id                 INT NOT NULL,
    category_id             INT NOT NULL,

    CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_transactions_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Budgets table 
CREATE TABLE budgets (
    budget_id         INT AUTO_INCREMENT PRIMARY KEY,
    budget_month      DATE NOT NULL,              
    amount_budgeted   DECIMAL(10, 2) NOT NULL,
    budget_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id           INT NOT NULL,
    category_id       INT NOT NULL,

    CONSTRAINT fk_budgets_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_budgets_category
        FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Make sure user has at most one budget per category per month
    CONSTRAINT uq_budget_per_month
        UNIQUE (user_id, category_id, budget_month)
);
