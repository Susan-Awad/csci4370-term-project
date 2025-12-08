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
     * Constructs a User with specified details.
     *
     * @param budget_id           the unique identifier of the budget
     * @param budget_month        the month of the budget
     * @param amount_budgeted         the amount of the budget
     * @param budget_created_at the creation date for the budget
     */
    public Budget(String budget_id, String budget_month, Double amount_budgeted, LocalDate budget_created_at) {
        this.budget_id = budget_id;
        this.budget_month = budget_month;
        this.amount_budgeted = amount_budgeted;
        this.budget_created_at = budget_created_at;
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
    public String getMonth() {
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
}
