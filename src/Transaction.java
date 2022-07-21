class Transaction {
    private Integer id,
            amount,
            superId;
    private String currency,
            category,
            description,
            transDate;
    private Boolean critical, paid;

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
