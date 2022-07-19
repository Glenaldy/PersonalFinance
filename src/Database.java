
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

    public int insertIntoTransaction(String[] input) throws SQLException {
        String table = "transactions";
        String sql = String.format(
                "INSERT INTO %s (currency, amount, category, description, trans_date) VALUES (?, ?, ?, ?, ?);", table);

        PreparedStatement prepSql = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        prepSql.setString(1, input[0].trim());
        prepSql.setString(2, input[1].trim());
        prepSql.setString(3, input[2].trim());
        prepSql.setString(4, input[3].trim());
        prepSql.setString(5, input[4].trim());

        if (prepSql.executeUpdate() == 0) {
            throw new SQLException("Inserting transaction failed.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Inserting transaction failed, no ID obtained.");
            }
        }
    }

    public Transaction insertTransaction(Transaction inputTransaction) throws SQLException {
        String table = "transactions";
        String sql = String.format(
                "INSERT INTO %s (currency, amount, category, description, trans_date) VALUES (?, ?, ?, ?, ?);", table);

        PreparedStatement prepSql = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        prepSql.setString(1, inputTransaction.currency);
        prepSql.setInt(2, inputTransaction.amount);
        prepSql.setString(3, inputTransaction.category);
        prepSql.setString(4, inputTransaction.description);
        prepSql.setString(5, inputTransaction.transDate);

        if (prepSql.executeUpdate() == 0) {
            throw new SQLException("Inserting transaction failed.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                inputTransaction.id = generatedKeys.getInt(1);

                return inputTransaction;
            } else {
                throw new SQLException("Inserting transaction failed, no ID obtained.");
            }
        }
    }

    public Transaction getTransactionFromId(int queryId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, queryId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            /*
             * Integer id = result.getInt("id");
             * String currency = result.getString("currency");
             * Integer amount = result.getInt("amount");
             * String category = result.getString("category");
             * String description = result.getString("description");
             * String transDate = result.getString("trans_date");
             * Integer superId = result.getInt("parent_id");
             * Boolean critical = result.getBoolean("critical");
             * Boolean paid = result.getBoolean("paid");
             */
            Transaction transaction = new Transaction(
                    result.getInt("id"),
                    result.getString("currency"),
                    result.getInt("amount"),
                    result.getString("category"),
                    result.getString("description"),
                    result.getString("trans_date"),
                    result.getInt("parent_id"),
                    result.getBoolean("critical"),
                    result.getBoolean("paid"));
            return transaction;
        }
        return null;

    }

    public Integer checkHasChild(Transaction transaction) throws SQLException {
        Integer queryId = transaction.getId();
        String sql = "SELECT COUNT(*) AS count FROM transactions WHERE parent_id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, queryId);
        ResultSet result = statement.executeQuery();

        return result.getInt(1);
    }

    public ArrayList<Transaction> getTransactionChild(Transaction transaction) throws SQLException {
        ArrayList<Transaction> childSet = new ArrayList<>();
        if (checkHasChild(transaction) <= 0) {
            return childSet;
        }

        Integer queryId = transaction.getId();
        String sql = "SELECT id FROM transactions WHERE parent_id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, queryId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Transaction childTransaction = getTransactionFromId(result.getInt(1));
            childSet.add(childTransaction);
        }
        return childSet;

    }

    // public ArrayList<Transaction> selectAll(String table, String argument) throws
    // SQLException {

    // ArrayList<Transaction> collection = new ArrayList<>();
    // String sql = String.format("SELECT * FROM %s;", table);
    // ResultSet result = statement.executeQuery(sql);
    // while (result.next()) {
    // Integer id = result.getInt("id");
    // String currency = result.getString("currency");
    // Integer amount = result.getInt("id");
    // String category = result.getString("category");
    // String description = result.getString("description");
    // String transDate = result.getString("trans_date");
    // Integer parentId = result.getInt("parent_id");

    // Transaction transaction = new Transaction(id, currency, amount, category,
    // description, transDate, parentId);

    // collection.add(transaction);
    // }
    // return collection;
    // }

    // public ArrayList<Transaction> selectAll(String table) throws SQLException {
    // return selectAll(table, "");
    // }

    public void selectAllParent() {
        String parentQuery = "SELECT parent.id AS parent_id, parent.category AS parent_category,parent.description AS parent_description,parent.amount AS parent_total_transaction, COUNT(child.id) AS count_child_transaction, (parent.amount - SUM(child.amount)) AS unlabeled_amount FROM transactions AS parent JOIN transactions AS child ON parent.id = child.parent_id WHERE parent.id = ? GROUP BY parent.id;";

        PreparedStatement prepSql = connection.prepareStatement(parentQuery, Statement.RETURN_GENERATED_KEYS);
        prepSql.setInt(1, id);
        ResultSet parentResult = prepSql.executeQuery();

        while (parentResult.next()) {
            System.out.println("reading transaction of " + id);
            Integer parentId = parentResult.getInt("parent_id");
            String parentCategory = parentResult.getString("parent_category");
            String parentDescription = parentResult.getString("parent_description");
            Integer parentTotalTransaction = parentResult.getInt("parent_total_transaction");
            Integer countChildTransaction = parentResult.getInt("count_child_transaction");
            Integer unlabeledAmount = parentResult.getInt("unlabeled_amount");

            System.out.printf("%s\t\t|\t%d\t|\t%s\t|\t%d\n", parentCategory, parentTotalTransaction, parentDescription,
                    unlabeledAmount);

            if (countChildTransaction > 0) {
                String childQuery = String.format(
                        "SELECT child.id AS child_id, child.amount AS child_transaction_amount, child.description AS child_description FROM transactions AS child WHERE parent_id = %d;",
                        parentId);
                ResultSet childResult = statement.executeQuery(childQuery);

                while (childResult.next()) {
                    Integer childID = childResult.getInt("child_id");
                    Integer childTransactionAmount = childResult.getInt("child_transaction_amount");
                    String childDescription = childResult.getString("child_description");

                    System.out.printf("└─%s\t|\t%d\n", childDescription, childTransactionAmount);
                }
            }
        }

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
            System.out.println("SQL Command Error, cannot create/open DB" + e.getMessage());
        }
    }

}
