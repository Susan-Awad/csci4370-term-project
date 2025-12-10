package proj.models;

public class Category {

    private int categoryId;
    private String categoryName;
    private String categoryType;   // "INCOME" or "EXPENSE"
    private String categoryColor;  // e.g. "red" or "#ff0000"
    private int userId;

    public Category() {
    }

    public Category(int categoryId, String categoryName, String categoryType,
                    String categoryColor, int userId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.categoryColor = categoryColor;
        this.userId = userId;
    }

    // Getters
    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}