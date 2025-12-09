package proj.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import proj.models.Budget;
import proj.models.Category;
import proj.models.User;


@Service
public class BudgetService {
    private final DataSource datasource;

    @Autowired
    public BudgetService(DataSource datasource) { 
        this.datasource = datasource; 
    }

    public int createBudget(int user_id, int category_id, String budget_month, Double amount_budgeted) throws SQLException{
        try (Connection c = datasource.getConnection()) {
            c.setAutoCommit(false);
            try {
                int budgetId;
                try (PreparedStatement ps = c.prepareStatement(
                    "INSERT into budgets (user_id, category_id, budget_month, amount_budgeted) values (?, ?, ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS
                )) {
                    ps.setInt(1, user_id);
                    ps.setInt(2, category_id);
                    LocalDate date = LocalDate.parse(budget_month + "-01");
                    ps.setDate(3, java.sql.Date.valueOf(date));
                    ps.setDouble(4, amount_budgeted);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        budgetId = keys.getInt(1);
                    }
                }
                c.commit();
                return budgetId;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public int updateBudget(int user_id, int budget_id, int category_id, String budget_month, Double amount_budgeted) throws SQLException{
        try (Connection c = datasource.getConnection()) {
            c.setAutoCommit(false);
            try {
                int budgetId;
                try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE budgets SET user_id = ?, category_id = ?, budget_month = ?, amount_budgeted = ? WHERE budget_id = ?",
                    Statement.RETURN_GENERATED_KEYS
                )) {
                    ps.setInt(1, user_id);
                    ps.setInt(2, category_id);
                    LocalDate date = LocalDate.parse(budget_month + "-01");
                    ps.setDate(3, java.sql.Date.valueOf(date));
                    ps.setDouble(4, amount_budgeted);
                    ps.setInt(5, budget_id);
                    ps.executeUpdate();
                    budgetId = budget_id;
                }
                c.commit();
                return budgetId;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void deleteBudget(int budget_id) throws SQLException {
        try (Connection c = datasource.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                "Delete from budgets where budget_id = ?")) {
                    ps.setInt(1, budget_id);
                    ps.executeUpdate();
            };
        }
    }

    public Double getMonthlyBudget(int userId, LocalDate month) throws SQLException {
        String sql = """
                SELECT SUM(amount_budgeted)
                FROM budgets
                WHERE user_id = ?
                    AND budget_month = ?
                """;

        try (Connection c = datasource.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    ps.setDate(2, java.sql.Date.valueOf(month));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getDouble(1);
                        } else {
                            return 0.00;
                        }
                    }
            }
        }
    }
}
