package proj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import proj.models.Budget;
import proj.models.Category;
import proj.services.UserService;
import proj.services.BudgetService;
import proj.services.CategoryService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.time.YearMonth;

import java.time.LocalDate;
import javax.sql.DataSource;

/**
 * Handles /budgets URL and its sub URLs.
 */
@Controller
@RequestMapping("/budgets")
public class BudgetController {

    private final UserService userService;
    private final DataSource dataSource; 
    private final BudgetService budgetService;
    private final CategoryService categoryService;

    @Autowired
    public BudgetController(UserService userService, DataSource dataSource, BudgetService budgetService, CategoryService categoryService) {
        this.userService = userService;
        this.dataSource = dataSource; 
        this.budgetService = budgetService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView budgetOfLoggedInUser() {
        System.out.println("User is attempting to view budget of the logged in user.");
        return budgetOfSpecificUser(userService.getLoggedInUser().getUserId());
    }

    @GetMapping("/{userId}")
    public ModelAndView budgetOfSpecificUser(@PathVariable("userId") String userId) {
        System.out.println("User is attempting to view budgets: " + userId);
        
        ModelAndView mv = new ModelAndView("budget_page");

        // Following line populates sample data.
        // You should replace it with actual data from the database.
        try {
            int user_id = Integer.parseInt(userId);

            List<Budget> budgets = getUserBudget(userId);
            LocalDate month = LocalDate.now().withDayOfMonth(1);
            List<Category> categories = categoryService.getCategoriesForUser(user_id);
            Double total_budget = budgetService.getMonthlyBudget(user_id, month);
            
            mv.addObject("monthlyBudget", total_budget);
            mv.addObject("categories", categories);
            mv.addObject("budgets", budgets);

            if (budgets.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            mv.addObject("monthlyBudget", 0.00);
        // If an error occured, you can set the following property with the
        // error message to show the error message to the user.
            String errorMessage = "Some error occured!";
            mv.addObject("errorMessage", errorMessage);
        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
        } //catch 
        return mv;
    }

    private List<Budget> getUserBudget(String userId) throws SQLException {
        final String sql = "SELECT B.budget_id, B.budget_month, B.budget_created_at, B.amount_budgeted, C.category_id, C.category_name, C.category_type " +
        "FROM budgets B " +
        "JOIN categories C ON B.category_id = C.category_id " +
        "WHERE B.user_id = ? " + 
        "ORDER BY B.budget_created_at DESC";

        List<Budget> out = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,Integer.parseInt(userId));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String month = rs.getDate("budget_month").toLocalDate().toString();
                        Double amount = rs.getBigDecimal("amount_budgeted").doubleValue();
                        LocalDate createdAt = rs.getTimestamp("budget_created_at").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        
                        Budget budget = new Budget (
                            String.valueOf(rs.getInt("budget_id")),
                            month,
                            amount,
                            createdAt,
                            rs.getInt("category_id"),
                            rs.getString("category_name"),
                            rs.getString("category_type")
                        );
                        out.add(budget);
                    } //while
                } //try - try
            } //try
            return out;
    } //getUserBudgets

    @PostMapping("/createbudget")
    public String createBudget(
        @RequestParam("budgetMonth") String budget_month, 
        @RequestParam("amount") Double amount_budgeted, 
        @RequestParam("categoryId") String category) {
            try {
                int user_id = Integer.parseInt(userService.getLoggedInUser().getUserId());
                int category_id = Integer.parseInt(category);
                budgetService.createBudget(user_id, category_id, budget_month, amount_budgeted);
            } catch (Exception e) {
                e.printStackTrace();
                String m = URLEncoder.encode("Failed to create budget.", StandardCharsets.UTF_8);
                return "redirect:/budgets/new?error=" + m;
            }

            return "redirect:/budgets";
    }

    @PostMapping("/updatebudget")
    public String updateBudget(
        @RequestParam("budget_id") String budgetId,
        @RequestParam("budgetMonth") String budget_month, 
        @RequestParam("amount") Double amount_budgeted, 
        @RequestParam("categoryId") String category) {
            try {
                int user_id = Integer.parseInt(userService.getLoggedInUser().getUserId());
                int budget_id = Integer.parseInt(budgetId);
                int category_id = Integer.parseInt(category);
                budgetService.updateBudget(user_id, budget_id, category_id, budget_month, amount_budgeted);
            } catch (Exception e) {
                e.printStackTrace();
                String m = URLEncoder.encode("Failed to update budget.", StandardCharsets.UTF_8);
                return "redirect:/budgets/new?error=" + m;
            }

            return "redirect:/budgets";
    }

    @PostMapping("/deletebudget")
    public String deleteBudget(@RequestParam("budget_id") String budgetId) {
        try {
            int budget_id = Integer.parseInt(budgetId);
            budgetService.deleteBudget(budget_id);
        } catch (Exception e) {
            e.printStackTrace();
            String m = URLEncoder.encode("Failed to delete budget.", StandardCharsets.UTF_8);
            return "redirect:/budgets/new?error=" + m;
        }

        return "redirect:/budgets";
    }
    
}
