public class WalletBalance {
    private Integer id;
    private String walletName;
    private Integer latestAmount;
    private String latestRecord;

    /**
     * @param id
     * @param walletName
     * @param latestAmount
     * @param latestRecord
     */
    public WalletBalance(Integer id, String walletName, Integer latestAmount, String latestRecord) {
        this.id = id;
        this.walletName = walletName;
        this.latestAmount = latestAmount;
        this.latestRecord = latestRecord;
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
     * @return the latestAmount
     */
    public Integer getLatestAmount() {
        return latestAmount;
    }

    /**
     * @return the latestRecord
     */
    public String getLatestRecord() {
        return latestRecord;
    }
}
