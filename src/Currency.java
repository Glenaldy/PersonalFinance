import java.sql.SQLException;
import java.util.ArrayList;

public class Currency {
    private Integer id;
    private String currencyName;

    public Currency(Integer id, String currencyName) {
        this.id = id;
        this.currencyName = currencyName;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public static ArrayList<String> getCurrencyList() {
        ArrayList<String> currencyList = new ArrayList<>();
        for (Currency currency : currencyList()) {
            currencyList.add(currency.getCurrencyName());
        }
        return currencyList;
    }

    private static ArrayList<Currency> currencyList() {
        ArrayList<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = GlobalEnvironmentVariable.db.getCurrencyList();
        } catch (SQLException e) {
        }
        return currencyList;
    }

}