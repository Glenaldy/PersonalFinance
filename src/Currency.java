import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    private static ArrayList<Currency> currencyList() {
        ArrayList<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = GlobalEnvironmentVariable.db.getCurrencyList();
        } catch (SQLException e) {
        }
        return currencyList;
    }

    public static ArrayList<String> getCurrencyList() {
        ArrayList<String> currencyList = new ArrayList<>();
        for (Currency currency : currencyList()) {
            currencyList.add(currency.getCurrencyName());
        }
        return currencyList;
    }

}
