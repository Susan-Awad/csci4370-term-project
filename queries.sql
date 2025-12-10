
-- Description: Registers a new user to the website
-- URL: http://localhost:8081/register
    insert into user (username, password, firstName, lastName) values (?, ?, ?, ?)

-- Description: Verifies the logged in user 
-- URL: http://localhost:8081/login
    select * from user where username = ?

-- Descriptoin: The query updates the users profile information based on the input.
-- URL: http://localhost:8081/profile
    UPDATE user SET firstName = ?, lastName = ?, username = ? WHERE userId = ?

-- Description: This query gets the username of the current user logged in.
-- URL: http://localhost:8081/profile
    SELECT username FROM user WHERE userId = ?

-- Description: This query gets all the user information from the user id.
-- URL:http://localhost:8081/profile
    SELECT userId, firstName, lastName FROM user WHERE userId = ?

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

-- Description: Updates the users budget information based on the given budget id from the form.
-- URL: http://localhost:8081/budgets
    UPDATE budgets 
    SET user_id = ?, category_id = ?, budget_month = ?, amount_budgeted = ? 
    WHERE budget_id = ?
    
-- Description: Retrieve total income for a given month for a specific user
-- URL: http://localhost:8081/home
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

-- Description: Retrieve total expenses for a given month for a specific user
-- URL: http://localhost:8081/home
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

-- Description: Retrieve the N most recent transactions for a user
-- URL: http://localhost:8081/transactions
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type,
    c.category_id, 
    c.category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.transaction_date DESC, t.transaction_id DESC
LIMIT ?;

-- Description: Retrieve all transactions for a user
-- URL: http://localhost:8081/transactions
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type,
    c.category_id, 
    c.category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.user_id = ?
ORDER BY t.transaction_date DESC, t.transaction_id DESC;

-- Description: Insert a new transaction
-- URL: http://localhost:8081/transactions
INSERT INTO transactions 
  (user_id, category_id, transaction_date, transaction_amount, transaction_description)
VALUES (?, ?, ?, ?, ?);

-- Description: Retrieve a single transaction by its ID
-- URL: http://localhost:8081/transactions
SELECT t.transaction_id,
    t.transaction_date,
    t.transaction_amount,
    t.transaction_description,
    c.category_type,
    c.category_id, 
    c.category_name
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE t.transaction_id = ?;

-- Description: Update an existing transaction
-- URL: http://localhost:8081/transactions
UPDATE transactions
SET category_id = ?,
    transaction_date = ?,
    transaction_amount = ?,
    transaction_description = ?
WHERE transaction_id = ?;

-- Description: Delete a transaction by ID
-- URL: http://localhost:8081/transactions
DELETE FROM transactions WHERE transaction_id = ?;

-- Description: Adds a category to a specific users category list
-- URL: http://localhost:8081/categories
    INSERT INTO categories (category_name, category_type, category_color, user_id) VALUES (?, ?, ?, ?)

-- Description: Deletes a category from the users category list
-- URL: http://localhost:8081/categories
    DELETE FROM categories WHERE category_id = ? AND user_id = ?

-- Description: Gets all of the users cateories and information
-- URL: http://localhost:8081/categories
    SELECT category_id, category_name, category_type, category_color, user_id
        FROM categories
        WHERE user_id = ?
        ORDER BY category_type, category_name
