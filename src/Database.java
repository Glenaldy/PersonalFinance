
import java.sql.Statement;
import java.util.ArrayList;

import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database(String databaseURL, String path) throws SQLException {
        connection = DriverManager.getConnection(databaseURL);
        statement = connection.createStatement();

        createOpenDB(connection, statement, path);
    }

    public ArrayList<Transaction> selectAll(String table, String argument) throws SQLException {
        ArrayList<Transaction> collection = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s;", table);
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            Integer id = result.getInt("id");
            String currency = result.getString("currency");
            Integer amount = result.getInt("id");
            String category = result.getString("category");
            String description = result.getString("description");
            String transDate = result.getString("trans_date");
            Integer superId = result.getInt("super_id");

            Transaction transaction = new Transaction(id, currency, amount, category, description, transDate, superId);

            collection.add(transaction);
        }
        return collection;
    }

    public ArrayList<Transaction> selectAll(String table) throws SQLException {
        return selectAll(table, "");
    }

    public void insertIntoTransaction(String[] input) throws SQLException {
        String table = "transactions";
        String sql = String.format(
                "INSERT INTO %s (currency, amount, category, description, trans_date) VALUES (?, ?, ?, ?, ?);", table);

        PreparedStatement prepSql = connection.prepareStatement(sql);

        prepSql.setString(1, input[0].trim());
        prepSql.setString(2, input[1].trim());
        prepSql.setString(3, input[2].trim());
        prepSql.setString(4, input[3].trim());
        prepSql.setString(5, input[4].trim());

        prepSql.executeUpdate();
        prepSql.close();
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
