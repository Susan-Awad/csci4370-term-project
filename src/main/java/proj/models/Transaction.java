package proj.models;

import java.time.LocalDate;

public class Transaction {
    /**
     * Unique identifier for the transaction.
     */
    private final String transactionId;

    /**
     * Date of the transaction.
     */
    private final LocalDate date;

    /**
     * Amount of the transaction.
     */
    private final Double amount;

    /**
     * Type of transaction.
     */
    private final String type;

    /**
     * Description for the transaction.
     */
    private final String description;

    /**
     * Constructs a Transaction with specified details.
     *
     * @param transactionId           the unique identifier of the transaction
     * @param date        the date of the transaction
     * @param amount         the amount for the transaction
     * @param type the type for the transaction
     * @param description the description for the transaction
     */
    public Transaction(String transactionId, LocalDate date, Double amount, String type, String description) {
        this.transactionId = transactionId;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    /**
     * Returns the transaction ID.
     *
     * @return the transaction ID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Returns the date of the transaction.
     *
     * @return the date of the transaction
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the amount of the transaction.
     *
     * @return the amount of the transaction
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Returns the type of the transaction.
     *
     * @return the type of the transaction
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the description of the transaction.
     *
     * @return the description of the transaction
     */
    public String getDescription() {
        return description;
    }


}
