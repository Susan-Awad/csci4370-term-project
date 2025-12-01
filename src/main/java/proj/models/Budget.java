package proj.models;

import java.time.LocalDate;

public class Budget {
    /**
     * Unique identifier for the budget.
     */
    private final String budgetId;

    /**
     * Month of the budget.
     */
    private final String month;

    /**
     * Amount budgeted of the budget.
     */
    private final Double amountBudgeted;

    /**
     * Created at for the budget.
     */
    private final LocalDate createdAt;

    /**
     * Constructs a User with specified details.
     *
     * @param budgetId           the unique identifier of the budget
     * @param month        the month of the budget
     * @param amountBudgeted         the amount of the budget
     * @param createdAt the creation date for the budget
     */
    public Budget(String budgetId, String month, Double amountBudgeted, LocalDate createdAt) {
        this.budgetId = budgetId;
        this.month = month;
        this.amountBudgeted = amountBudgeted;
        this.createdAt = createdAt;
    }

    /**
     * Returns the budget ID.
     *
     * @return the budget ID
     */
    public String getBudgetId() {
        return budgetId;
    }

    /**
     * Returns the month of the budget.
     *
     * @return the month of the budget
     */
    public String getMonth() {
        return month;
    }

    /**
     * Returns the amount budgeted of the budget.
     *
     * @return the amount budgeted of the budget
     */
    public Double getAmountBudgeted() {
        return amountBudgeted;
    }

    /**
     * Returns the creation date for the budget.
     *
     * @return the creation date for the budget
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
