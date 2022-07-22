import java.util.ArrayList;

/**
 * Transaction class stores the transaction either from other method or from
 * database query that will store all of the information needed.
 */
public class Transaction {
    private Integer id,
            amount,
            superId;
    private String currency,
            category,
            description,
            transDate;
    private Boolean critical, paid;

    /**
     * Transaction object constructor that will create the object according tot he
     * parameter given.
     * 
     * @param id
     * @param currency
     * @param amount
     * @param category
     * @param description
     * @param transDate
     * @param superId
     * @param critical
     * @param paid
     */
    public Transaction(Integer id, String currency, Integer amount, String category, String description,
            String transDate, Integer superId, Boolean critical, Boolean paid) {
        this.id = id;
        this.currency = currency;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.transDate = transDate;
        this.superId = superId;
        this.critical = critical;
        this.paid = paid;
    }

    /**
     * Static method that accepts argument of ArrayList of Transaction objects and
     * the argument to change the mode.
     * There are 4 modes, critical, paid, not critical, and not paid.
     * It will loop through the ArrayList and check the critical and paid variable
     * inside.
     * Appropriate transactions will be returned as a new ArrayList.
     * 
     * @param transactions
     * @param argument
     * @return ArrayList of Transaction objects that's already sorted according to
     *         the critical and paid field.
     */
    public static ArrayList<Transaction> searchByField(ArrayList<Transaction> transactions, String argument) {
        ArrayList<Transaction> output = new ArrayList<>();
        for (Transaction transaction : transactions) {
            switch (argument.toLowerCase()) {
                case "critical":
                    if (transaction.getCritical())
                        output.add(transaction);
                    break;
                case "paid":
                    if (transaction.getPaid())
                        output.add(transaction);
                    break;
                case "notcritical":
                    if (!transaction.getCritical())
                        output.add(transaction);
                    break;
                case "notpaid":
                    if (!transaction.getPaid())
                        output.add(transaction);
                    break;
                default:
                    break;
            }
        }
        return output;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return the superId
     */
    public Integer getSuperId() {
        return superId;
    }

    /**
     * @param superId the superId to set
     */
    public void setSuperId(Integer superId) {
        this.superId = superId;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the transDate
     */
    public String getTransDate() {
        return transDate;
    }

    /**
     * @param transDate the transDate to set
     */
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    /**
     * @return the critical
     */
    public Boolean getCritical() {
        return critical;
    }

    /**
     * @param critical the critical to set
     */
    public void setCritical(Boolean critical) {
        this.critical = critical;
    }

    /**
     * @return the paid
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

}
