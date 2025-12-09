package proj.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import proj.models.Transaction;

@Service
public class TransactionService {
    
    @Autowired
    private JdbcTemplate jdbc;

    private static class TransactionMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transaction(
                String.valueOf(rs.getInt("transaction_id")),                 // transactionId (String)
                rs.getDate("transaction_date").toLocalDate(),                // date
                rs.getDouble("transaction_amount"),                          // amount
                rs.getString("category_type"),                               // type (INCOME/EXPENSE from categories table)
                rs.getString("transaction_description"),
                rs.getInt("category_id"),
                rs.getString("category_name")                   
            );
        }
    }

    public BigDecimal getTotalIncomeForMonth(int userId, LocalDate start, LocalDate end) {
        String sql = """
                SELECT SUM(transaction_amount)
                FROM transactions
                WHERE user_id = ?
                    AND category_id IN (
                        SELECT category_id
                        FROM categories
                        WHERE user_id = ? AND category_type = 'INCOME'
                    )
                    AND transaction_date >= ?
                    AND transaction_date < ?
                """;

        BigDecimal result = jdbc.queryForObject(sql, BigDecimal.class, userId, userId, start, end);

        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenseForMonth(int userId, LocalDate start, LocalDate end) {
        String sql = """
                SELECT SUM(transaction_amount)
                FROM transactions
                WHERE user_id = ?
                    AND category_id IN (
                        SELECT category_id
                        FROM categories
                        WHERE user_id = ? AND category_type = 'EXPENSE'
                    )
                    AND transaction_date >= ?
                    AND transaction_date < ?
                """;
        BigDecimal result = jdbc.queryForObject(sql, BigDecimal.class, userId, userId, start, end);

        return result != null ? result : BigDecimal.ZERO;
    }

    public List<Transaction> getRecentTransactions(int userId, int limit) {

        String sql = """
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
            LIMIT ?
        """;

        return jdbc.query(sql, new TransactionMapper(), userId, limit);
    }

    public List<Transaction> getAllTransactionsForUser(int userId) {

        String sql = """
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
        """;

        return jdbc.query(sql, new TransactionMapper(), userId);
    }

    public void addTransaction(int userId, int categoryId, LocalDate date, Double amount, String description) {
        String sql = """
            INSERT INTO transactions 
              (user_id, category_id, transaction_date, transaction_amount, transaction_description)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbc.update(sql, userId, categoryId, date, amount, description);
    }

    public Transaction getTransactionById(int transactionId) {
        String sql = """
            SELECT t.transaction_id,
                   t.transaction_date,
                   t.transaction_amount,
                   t.transaction_description,
                   c.category_type,
                   c.category_id, 
                   c.category_name
            FROM transactions t
            JOIN categories c ON t.category_id = c.category_id
            WHERE t.transaction_id = ?
        """;
        return jdbc.queryForObject(sql, new TransactionMapper(), transactionId);
    }

    public void updateTransaction(int transactionId, int categoryId, LocalDate date, Double amount, String description) {
        String sql = """
            UPDATE transactions
            SET category_id = ?,
                transaction_date = ?,
                transaction_amount = ?,
                transaction_description = ?
            WHERE transaction_id = ?
        """;
        jdbc.update(sql, categoryId, date, amount, description, transactionId);
    }

    public void deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        jdbc.update(sql, transactionId);
    }
}

