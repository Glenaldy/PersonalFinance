import java.sql.SQLException;
import java.util.ArrayList;

public class Wallet {
    private Integer id;
    private String currency;
    private String walletName;

    public Wallet(Integer id, String currency, String walletName) {
        this.id = id;
        this.currency = currency;
        this.walletName = walletName;
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

    public static ArrayList<Wallet> walletList() {
        ArrayList<Wallet> walletList = new ArrayList<>();
        try {
            walletList = GlobalEnvironmentVariable.db.getWalletList(GlobalEnvironmentVariable.currency);
        } catch (SQLException e) {
        }
        return walletList;
    }
}
