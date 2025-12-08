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

