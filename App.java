import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";
        String jdbcUrl = "jdbc:sqlite:";
        String database = "database/personalfinance.db";

        String databaseURL = jdbcUrl + "/" + PATH + "/" + database;

        Database db = new Database(databaseURL, PATH);
        // READ

        // Give me all the transaction
        ArrayList<Transaction> getParent = db.selectAll("transactions");

        System.out.println(getParent);

        String[] test1 = { "yen", "-5000", "conbini", "bought this today", "2022/07/19" };
        // ScannerInput.ScanTransaction()
        // yen, -5000, conbini, bought this today, 2022/07/19
        db.insertIntoTransaction(test1);

        System.out.println();
    }
}
