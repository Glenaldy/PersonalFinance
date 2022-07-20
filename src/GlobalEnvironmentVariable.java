import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GlobalEnvironmentVariable {
    static String currency = "yen";

    public static String getDateToday() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()).toString();
    }
}
