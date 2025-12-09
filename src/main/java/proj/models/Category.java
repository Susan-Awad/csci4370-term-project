package proj.models;

public class Category {

    private int categoryId;
    private String categoryName;
    private String categoryType;   
    private String categoryColor;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
