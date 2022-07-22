import java.sql.SQLException;
import java.util.ArrayList;

/** Currency class stores the currency available */

public class Currency {
    private Integer id;
    private String currencyName;

    /**
     * Constructor of the currency object according tot he parameter given.
     * 
     * @param id
     * @param currencyName
     */
    public Currency(Integer id, String currencyName) {
        this.id = id;
        this.currencyName = currencyName;
    }

    /**
     * Static method that will call the currencyList() method and go through the
     * ArrayList it gets and get the currency_name of the currency object.
     * 
     * @return ArrayList of String of currency name
     */
    public static ArrayList<String> getCurrencyList() {
        ArrayList<String> currencyList = new ArrayList<>();
        for (Currency currency : currencyList()) {
            currencyList.add(currency.getCurrencyName());
        }
        return currencyList;
    }

    /**
     * Static method that will access the database in the GlobalEnvironmentVariable
     * and return it as ArrayList of currency objects.
     * 
     * @return ArrayList of currency objects
     */
    private static ArrayList<Currency> currencyList() {
        ArrayList<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = GlobalEnvironmentVariable.db.getCurrencyList();
        } catch (SQLException e) {
        }
        return currencyList;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

}
