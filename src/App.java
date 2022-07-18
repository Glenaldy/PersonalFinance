import java.sql.*;

public class App {
    public static void main(String[] args) throws Exception {
        final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";
        String jdbcUrl = "jdbc:sqlite:";
        String database = "database/personalfinance.db";

        String databaseURL = jdbcUrl + "/" + PATH + "/" + database;
        Connection connection = DriverManager.getConnection(databaseURL);
        Statement statement = connection.createStatement();
        createOpenDB(connection, statement, PATH);

        Database db = new Database(connection, statement);
        // READ
        db.selectFrom("transactions");

        System.out.printf("Hello, welcome to the Personal Finance\n What woudl you like to do?\n");
        System.out.println("[1] Insert new Stu");
    }

    private static void createOpenDB(Connection connection, Statement statement, String PATH) {
        try {
            String selectStatement = "SELECT * FROM transactions;";
            try {
                statement.executeQuery(selectStatement);
                System.out.printf("Database already exists.\nAccessing...\n");
            } catch (Exception e) {
                System.out.printf("Database doesn't exist, creating database...\n");
                String sql = ScriptRunner.runScript(PATH + "/database/initialExecution.sql");
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("SQL Command Error" + e.getMessage());
        }
    }
}
