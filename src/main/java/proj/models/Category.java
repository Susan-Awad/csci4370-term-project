package proj.models;

public class Category {
    /**
     * Unique identifier for the category.
     */
    private final String categoryId;

    /**
     * Name of the category.
     */
    private final String catName;

    /**
     * Color of the category.
     */
    private final String catColor;

    /**
     * Constructs a User with specified details.
     *
     * @param categoryId           the unique identifier of the category
     * @param name        the name of the category
     * @param catColor       the color of the category
     */
    public Category(String categoryId, String catName, String catColor) {
        this.categoryId = categoryId;
        this.catName = catName;
        this.catColor = catColor;
    }

    /**
     * Returns the category ID.
     *
     * @return the category ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Returns the name of the category.
     *
     * @return the name of the category
     */
    public String getCatName() {
        return catName;
    }

    /**
     * Returns the color of the category.
     *
     * @return the color of the category
     */
    public String getCatColor() {
        return catColor;
    }

}
