import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GlobalEnvironmentVariable {
    private static final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";

    private static String jdbcUrl = "jdbc:sqlite:";
    private static String database = "database/personalfinance.db";
    private static String databaseURL = jdbcUrl + "/" + PATH + "/" + database;

    public static Database db;
    public static String currency;
    public static String currencyList;

    static {
        try {
            db = new Database(databaseURL, PATH);
            currency = db.getCurrencyList().get(0).getCurrencyName();
        } catch (Exception e) {
            System.out.println("CANNOT GET THE DATABASE");
        }
    }

    public static String getDateToday() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).toString();
    }

}
