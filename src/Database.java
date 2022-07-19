
import java.sql.Statement;
import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database(String databaseURL, String path) throws SQLException {
        connection = DriverManager.getConnection(databaseURL);
        statement = connection.createStatement();

        createOpenDB(connection, statement, path);
    }

    public ArrayList<Transaction> selectFrom(String table, String argument) throws SQLException {
        ArrayList<Transaction> collection = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", table);
        ResultSet result = statement.executeQuery(sql);
        ResultSetMetaData metadata = result.getMetaData();
        int columnCount = metadata.getColumnCount();

        while (result.next()) {
            Integer id = result.getInt("id");
            String currency = result.getString("currency");
            Integer amount = result.getInt("id");
            String category = result.getString("category");
            String description = result.getString("description");
            String transDate = result.getString("transDate");
            Integer superId = result.getInt("super_id");

            Transaction transaction = new Transaction(id, currency, amount, category, description, transDate, superId);

            collection.add(transaction);
            // for (int i = 1; i <= columnCount; i++) {
            // String columnName = metadata.getColumnName(i);

            // }
        }
        return collection;
    }

    public ArrayList<Transaction> selectFrom(String table) throws SQLException {
        return selectFrom(table, "");
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
