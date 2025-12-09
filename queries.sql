
-- Description: Registers a new user to the website
-- URL: http://localhost:8081/register
insert into user (username, password, firstName, lastName) values (?, ?, ?, ?)

-- Description: Verifies the logged in user 
-- URL: http://localhost:8081/login
select * from user where username = ?

-- Description: Retrieves all the users budgets they have created.
-- URL: http://localhost:8081/budgets
    SELECT B.budget_id, B.budget_month, B.budget_created_at, B.amount_budgeted, C.category_name, C.category_type
    FROM budgets B
    JOIN categories C ON B.category_id = C.category_id
    WHERE B.user_id = ?
    ORDER BY B.budget_created_at DESC

-- Description: Creates a budget for s epcific user and category,
--              inserting all the required fields.
-- URL: http://localhost:8081/budgets
    INSERT into budgets (user_id, category_id, budget_month, amount_budgeted) values (?, ?, ?, ?) 

-- Description: Deletes a users budget information for a specific budget
-- URL: http://localhost:8081/budgets
    Delete from budgets where budget_id = ?

-- Description: Uses an aggregate query to find the sum of the budgets for the current month and returns the value
-- URL: http://localhost:8081/budgets
    SELECT SUM(amount_budgeted)
        FROM budgets
        WHERE user_id = ?
            AND budget_month = ?
-- Retrieve total income for a given month for a specific user
  SELECT SUM(transaction_amount)
  FROM transactions
  WHERE user_id = ?
    AND category_id IN (
        SELECT category_id
        FROM categories
        WHERE user_id = ? AND category_type = 'INCOME'
    )
    AND transaction_date >= ?
    AND transaction_date < ?;

-- Retrieve total expenses for a given month for a specific user
  SELECT SUM(transaction_amount)
  FROM transactions
  WHERE user_id = ?
    AND category_id IN (
      SELECT category_id
      FROM categories
      WHERE user_id = ? AND category_type = 'EXPENSE'
    )
  AND transaction_date >= ?
  AND transaction_date < ?;

-- Retrieve the N most recent transactions for a user
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.transaction_date DESC, t.transaction_id DESC
LIMIT ?;

-- Retrieve all transactions for a user
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.transaction_date DESC, t.transaction_id DESC;

-- Insert a new transaction
INSERT INTO transactions 
  (user_id, category_id, transaction_date, transaction_amount, transaction_description)
VALUES (?, ?, ?, ?, ?);

-- Retrieve a single transaction by its ID
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.transaction_id = ?;

-- Update an existing transaction
UPDATE transactions
SET category_id = ?,
    transaction_date = ?,
    transaction_amount = ?,
    transaction_description = ?
WHERE transaction_id = ?;

-- Delete a transaction by ID
DELETE FROM transactions WHERE transaction_id = ?;

