package proj.models;

import java.time.LocalDate;

public class Budget {
    /**
     * Unique identifier for the budget.
     */
    private final String budget_id;

    /**
     * Month of the budget.
     */
    private final String budget_month;

    /**
     * Amount budgeted of the budget.
     */
    private final Double amount_budgeted;

    /**
     * Created at for the budget.
     */
    private final LocalDate budget_created_at;

    /**
     * Category id for the budget.
     */
    private int categoryId;

    /**
     * Category name for the budget.
     */
    private String category_name;

    /**
     * category type for the budget.
     */
    private String category_type;

    /**
     * Constructs a User with specified details.
     *
     * @param budget_id           the unique identifier of the budget
     * @param budget_month        the month of the budget
     * @param amount_budgeted         the amount of the budget
     * @param budget_created_at the creation date for the budget
     * @param category_id the category id for the budget
     * @param category_type the category type for the budget
     * @param category_name the category name for the budget
     */
    public Budget(String budget_id, String budget_month, Double amount_budgeted, LocalDate budget_created_at,
        int categoryId, String category_name, String category_type
    ) {
        this.budget_id = budget_id;
        this.budget_month = budget_month;
        this.amount_budgeted = amount_budgeted;
        this.budget_created_at = budget_created_at;
        this.categoryId = categoryId;
        this.category_name = category_name;
        this.category_type = category_type;
    }

    /**
     * Returns the budget ID.
     *
     * @return the budget ID
     */
    public String getBudgetId() {
        return budget_id;
    }

    /**
     * Returns the month of the budget.
     *
     * @return the month of the budget
     */
    public String getBudgetMonth() {
        return budget_month;
    }

    /**
     * Returns the amount budgeted of the budget.
     *
     * @return the amount budgeted of the budget
     */
    public Double getAmountBudgeted() {
        return amount_budgeted;
    }

    /**
     * Returns the creation date for the budget.
     *
     * @return the creation date for the budget
     */
    public LocalDate getCreatedAt() {
        return budget_created_at;
    }

    /**
     * Returns the category id for the budget.
     *
     * @return the category id for the budget
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Returns the category name for the budget.
     *
     * @return the category name for the budget
     */
    public String getCategoryName() {
        return category_name;
    }

    /**
     * Returns the categoy type for the budget.
     *
     * @return the category type for the budget
     */
    public String getCategoryType() {
        return category_type;
    }
}
