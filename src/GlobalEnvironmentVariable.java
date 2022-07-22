import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * GlobalEnvironmentVariable is global class that will first attempt to create 2
 * static variables database and the currency by constructing a new database
 * object. All of the other classes and object needed to interact with the
 * database or the currency mode will refer to this class.
 */
public class GlobalEnvironmentVariable {
    /**
     * PUT THE LOCATION OF YOUR PROJECT PATH INTO PATH VARIABLE TO BE ABLE TO RUN
     * ALL THE PROGRAMS
     */

    private static final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";

    private static String jdbcUrl = "jdbc:sqlite:";
    private static String database = "database/personalfinance.db";
    private static String databaseURL = jdbcUrl + "/" + PATH + "/" + database;

    public static Database db;
    public static String currency;

    static {
        try {
            db = new Database(databaseURL, PATH);
            currency = db.getCurrencyList().get(0).getCurrencyName();
        } catch (Exception e) {
            System.out.println("CANNOT GET THE DATABASE");
        }
    }

    /**
     * Static method that can be called anywhere to give current date.
     * It will call LocalDate.now() method and format it to the yyyy-MM-dd.
     * The reason is that sqlite only supports this date format for storage.
     * 
     * @return String of date today according to system date.
     */
    public static String getDateToday() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).toString();
    }

}
