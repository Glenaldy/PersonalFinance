import java.sql.*;
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
        ArrayList<Transaction> getParent = db.selectFrom("transactions", "");

        System.out.println(getParent);

    }

    // private static void createOpenDB(Connection connection, Statement statement,
    // String PATH) {
    // try {
    // String selectStatement = "SELECT * FROM transactions;";
    // try {
    // statement.executeQuery(selectStatement);
    // System.out.printf("Database already exists.\nAccessing...\n");
    // } catch (Exception e) {
    // System.out.printf("Database doesn't exist, creating database...\n");
    // String sql = ScriptRunner.runScript(PATH + "/database/initialExecution.sql");
    // statement.executeUpdate(sql);
    // }
    // } catch (SQLException e) {
    // System.out.println("SQL Command Error" + e.getMessage());
    // }
    // }
}
