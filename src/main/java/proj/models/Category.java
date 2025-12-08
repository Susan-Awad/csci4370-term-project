package proj.models;

public class Category {
    /**
     * Unique identifier for the category.
     */
    private final String category_id;

    /**
     * Name of the category.
     */
    private final String category_name;

    /**
     * Type of the category.
     */
    private final String category_type;

    /**
     * Color of the category.
     */
    private final String category_color;

    /**
     * Constructs a User with specified details.
     *
     * @param category_id           the unique identifier of the category
     * @param category_name        the name of the category
     * @param category_type          the type of the category
     * @param category_color       the color of the category
     */
    public Category(String category_id, String category_name, String category_type,  String category_color) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_type = category_type;
        this.category_color = category_color;
    }

    /**
     * Returns the category ID.
     *
     * @return the category ID
     */
    public String getCategoryId() {
        return category_id;
    }

    /**
     * Returns the name of the category.
     *
     * @return the name of the category
     */
    public String getCatName() {
        return category_name;
    }

    /**
     * Returns the type of the category.
     *
     * @return the type of the category
     */
    public String getCategoryType() {
        return category_type;
    }

    /**
     * Returns the color of the category.
     *
     * @return the color of the category
     */
    public String getCatColor() {
        return category_color;
    }

}
