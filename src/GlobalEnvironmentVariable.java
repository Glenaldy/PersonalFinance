import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GlobalEnvironmentVariable {
    private static final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";

    private static String jdbcUrl = "jdbc:sqlite:";
    private static String database = "database/personalfinance.db";
    private static String databaseURL = jdbcUrl + "/" + PATH + "/" + database;

    public static Database db;
    static {
        try {
            db = new Database(databaseURL, PATH);
        } catch (Exception e) {
            System.out.println("CANNOT GET THE DATABASE");
        }
    }

    static String currency = "yen";
    static String currencyList;

    // private static ArrayList<Wallet> walletList = ;
    // static {
    // try {
    // walletList = db.getWalletList(GlobalEnvironmentVariable.currency);
    // } catch (Exception e) {
    // System.out.println("Cannot Get Wallet List");
    // }
    // }

    static String wallet = walletList().stream().map(Wallet::getWalletName).collect(Collectors.joining(", "));

    private static ArrayList<Wallet> walletList() {
        ArrayList<Wallet> walletList = new ArrayList<>();

        try {
            walletList = db.getWalletList(currency);
        } catch (SQLException e) {

        }

        return walletList;

    }

    public static String getDateToday() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()).toString();
    }

}
