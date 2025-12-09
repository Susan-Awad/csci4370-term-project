package proj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import proj.models.Category;
import proj.services.UserService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles /categories URL and its sub URLs.
 * Shows, creates, and deletes budget categories for the logged-in user.
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final UserService userService;
    private final DataSource dataSource;

    @Autowired
    public CategoryController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    /**
     * Helper: get the logged in userId as an Integer.
     * userService.getLoggedInUser().getUserId() returns a String,
     * so we parse it here.
     */
    private Integer getLoggedInUserId() {
        if (userService.getLoggedInUser() == null) {
            return null;
        }
        try {
            return Integer.parseInt(userService.getLoggedInUser().getUserId());
        } catch (NumberFormatException e) {
            // If something is wrong with the id in session, treat as not logged in
            return null;
        }
    }

    /**
     * GET /categories
     * Show all categories for the logged-in user.
     */
    @GetMapping
    public ModelAndView showCategories() {
        System.out.println("User is attempting to view categories of the logged in user.");

        Integer userId = getLoggedInUserId();
        if (userId == null) {
            // If not logged in, send them to login
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mv = new ModelAndView("category_page");

        try {
            List<Category> categories = loadCategoriesForUser(userId);
            mv.addObject("categories", categories);
            mv.addObject("isNoContent", categories.isEmpty());
        } catch (SQLException e) {
            e.printStackTrace();
            mv.addObject("errorMessage", "Could not load categories. Please try again later.");
            mv.addObject("isNoContent", true);
        }

        return mv;
    }

    /**
     * POST /categories
     * Add a new category for the logged-in user.
     */
    @PostMapping
    public String addCategory(@RequestParam("categoryName") String name,
                              @RequestParam("categoryType") String type,
                              @RequestParam(value = "categoryColor", required = false) String color) {

        Integer userId = getLoggedInUserId();
        if (userId == null) {
            return "redirect:/login";
        }

        if (color == null) {
            color = "";
        }

        // Ignore empty names
        if (name == null || name.trim().isEmpty()) {
            return "redirect:/categories";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO categories (category_name, category_type, category_color, user_id) " +
                             "VALUES (?, ?, ?, ?)")) {

            ps.setString(1, name.trim());
            ps.setString(2, type);
            ps.setString(3, color.trim());
            ps.setInt(4, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/categories";
    }

    /**
     * POST /categories/{categoryId}/delete
     * Delete a category that belongs to the logged-in user.
     */
    @PostMapping("/{categoryId}/delete")
    public String deleteCategory(@PathVariable("categoryId") int categoryId) {

        Integer userId = getLoggedInUserId();
        if (userId == null) {
            return "redirect:/login";
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM categories WHERE category_id = ? AND user_id = ?")) {

            ps.setInt(1, categoryId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/categories";
    }

    // Load all categories for a user from the database.
    private List<Category> loadCategoriesForUser(int userId) throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = """
            SELECT category_id, category_name, category_type, category_color, user_id
            FROM categories
            WHERE user_id = ?
            ORDER BY category_type, category_name
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category();
                    c.setCategoryId(rs.getInt("category_id"));
                    c.setCategoryName(rs.getString("category_name"));
                    c.setCategoryType(rs.getString("category_type"));
                    c.setCategoryColor(rs.getString("category_color"));
                    c.setUserId(rs.getInt("user_id"));
                    categories.add(c);
                }
            }
        }

        return categories;
    }
}