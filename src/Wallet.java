import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Wallet class stores the information of the wallet name and the currency of
 * the particular wallet and id of the wallet in the database.
 */

public class Wallet {
    private Integer id;
    private String currency;
    private String walletName;

    /**
     * Constructor of wallet object accordingt to the given parameter.
     * 
     * @param id
     * @param currency
     * @param walletName
     */
    public Wallet(Integer id, String currency, String walletName) {
        this.id = id;
        this.currency = currency;
        this.walletName = walletName;
    }

    /**
     * Static method that can be called to give back all the wallet available in the
     * currency determined by the currency variable in the
     * GlobalEnvironmentVariable.
     * 
     * @return
     */
    public static ArrayList<Wallet> walletList() {
        ArrayList<Wallet> walletList = new ArrayList<>();
        try {
            walletList = GlobalEnvironmentVariable.db.getWalletList(GlobalEnvironmentVariable.currency);
        } catch (SQLException e) {
        }
        return walletList;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getWalletName() {
        return this.walletName;
    }

}
