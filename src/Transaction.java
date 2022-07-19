class Transaction {
    Integer id,
            amount,
            superId;
    String currency,
            category,
            description,
            transDate;
    Boolean critical, paid;

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

    public Integer getId() {
        return this.id;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Integer getSuperId() {
        return this.superId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public Boolean getCritical() {
        return this.critical;
    }

    public Boolean getPaid() {
        return this.paid;
    }

}
