/**
 * Wallet Balance stores the balance of the wallet and the recordDate
 */

public class WalletBalance {
    private Integer id;
    private String walletName;
    private Integer amount;
    private String recordDate;

    /**
     * @param id
     * @param walletName
     * @param amount
     * @param recordDate
     */
    public WalletBalance(Integer id, String walletName, Integer amount, String recordDate) {
        this.id = id;
        this.walletName = walletName;
        this.amount = amount;
        this.recordDate = recordDate;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the walletName
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @return the recordDate
     */
    public String getRecordDate() {
        return recordDate;
    }
}
