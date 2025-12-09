package proj.services;

import org.springframework.stereotype.Service;
import proj.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Service for CRUD operations on categories
@Service
public class CategoryService {

    private final DataSource dataSource;

    public CategoryService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Get all categories for a given user
    public List<Category> getCategoriesForUser(int userId) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Create a new category
    public void createCategory(String name, String type, String color, int userId) {
        String sql = """
            INSERT INTO categories (category_name, category_type, category_color, user_id)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, color);
            ps.setInt(4, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a category that belongs to this user
    public void deleteCategory(int categoryId, int userId) {
        String sql = "DELETE FROM categories WHERE category_id = ? AND user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}