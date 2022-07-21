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

    static {
        try {
            db = new Database(databaseURL, PATH);
        } catch (Exception e) {
            System.out.println("CANNOT GET THE DATABASE");
        }
    }

    static String currency = "yen";
    static String currencyList;

    public static String getDateToday() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()).toString();
    }

    public static String getYearMonth(String date, int monthModifier) {
        SimpleDateFormat dashDate = new SimpleDateFormat("yyyy-MM-dd");
        Date convert;
        try {
            convert = dashDate.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(convert);

            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = (cal.get(Calendar.MONTH) + 1) < 10
                    ? 0 + String.valueOf(cal.get(Calendar.MONTH) + 1 + monthModifier)
                    : String.valueOf(cal.get(Calendar.MONTH) + 1 + monthModifier);
            return String.format("%s-%s", year, month);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getYearMonth(String date) {
        return getYearMonth(date, 0);
    }

}
