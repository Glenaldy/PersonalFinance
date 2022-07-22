
import java.util.ArrayList;
import java.util.HashSet;

import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public Database(String databaseURL, String path) throws SQLException {
        connection = DriverManager.getConnection(databaseURL);
        statement = connection.createStatement();

        createOpenDB(connection, statement, path);
    }

    public ArrayList<Transaction> getTransactionFromYearMonth(String yearMonth, Boolean critical, Boolean paid)
            throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE strftime('%Y-%m', trans_date) = ? AND currency = ? AND parent_id IS NULL AND critical = ? AND paid = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, yearMonth);
        statement.setString(2, GlobalEnvironmentVariable.currency);
        statement.setBoolean(3, critical);
        statement.setBoolean(4, paid);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Transaction transaction = getTransactionFromId(result.getInt(1));
            transactions.add(transaction);
        }

        return transactions;
    }

    public ArrayList<Transaction> getTransactionFromYearMonth(String yearMonth) throws SQLException {
        ArrayList<Transaction> output = new ArrayList<>();
        output.addAll(getTransactionFromYearMonth(yearMonth, true, true));
        output.addAll(getTransactionFromYearMonth(yearMonth, false, true));
        output.addAll(getTransactionFromYearMonth(yearMonth, true, false));
        output.addAll(getTransactionFromYearMonth(yearMonth, false, false));
        return output;
    }

    public void updateWalletBalance(Integer id, Integer updateAmount, String date) throws SQLException {
        String sql = "UPDATE wallet_balance SET amount = ? WHERE record_date = ? AND record_wallet = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, updateAmount);
        statement.setString(2, date);
        statement.setInt(3, id);
        statement.executeUpdate();

        sql = "SELECT changes() AS count;";
        statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        int count = 0;
        while (result.next()) {
            count = result.getInt("count");
        }

        if (count == 0) {
            sql = "INSERT INTO wallet_balance (record_wallet, record_date, amount) VALUES (?, ?, ?);";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, date);
            statement.setInt(3, updateAmount);
            statement.executeUpdate();
        }
    }

    public void insertWallet(String newWallet) throws SQLException {
        String sql = "INSERT INTO wallets (currency, wallet_name) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, GlobalEnvironmentVariable.currency);
        statement.setString(2, newWallet);
        statement.execute();
        ResultSet result = statement.getGeneratedKeys();
        if (result.next()) {
            updateWalletBalance(result.getInt(1), 0, GlobalEnvironmentVariable.getDateToday());
        }
    }

    public void deleteWallet(Integer id) throws SQLException {
        String sql = "DELETE FROM wallets WHERE id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
    }

    public void changeTransactionBooleanField(Transaction transaction, String field) throws SQLException {
        String sql;
        Boolean bool;
        if (field.equals("critical")) {
            sql = "UPDATE transactions SET critical = ? WHERE transactions.id = ?;";
            bool = !transaction.getCritical();
        } else if (field.equals("paid")) {
            sql = "UPDATE transactions SET paid = ? WHERE transactions.id = ?;";
            bool = !transaction.getPaid();
        } else {
            throw new SQLException();
        }
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBoolean(1, bool);
        for (Transaction child : GlobalEnvironmentVariable.db.getTransactionChild(transaction)) {
            statement.setInt(2, child.getId());
            statement.executeUpdate();
        }

        statement.setInt(2, transaction.getId());
        statement.executeUpdate();
    }

    public ArrayList<WalletBalance> getWalletBalance(Boolean latest) throws SQLException {
        String sql;
        if (latest) {
            sql = "SELECT wallets.id, wallets.wallet_name, wallet_balance.amount, wallet_balance.record_date FROM wallet_balance JOIN wallets ON wallet_balance.record_wallet = wallets.id WHERE wallets.currency = ? AND record_date = (SELECT max(record_date) FROM wallet_balance AS latest WHERE strftime('%Y-%m', record_date) = ? AND wallets.id = latest.record_wallet);";
        } else {
            sql = "SELECT wallets.id, wallets.wallet_name, wallet_balance.amount, wallet_balance.record_date FROM wallet_balance JOIN wallets ON wallet_balance.record_wallet = wallets.id WHERE wallets.currency = ? AND record_date = (SELECT min(record_date) FROM wallet_balance AS latest WHERE strftime('%Y-%m', record_date) = ? AND wallets.id = latest.record_wallet);";
        }

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, GlobalEnvironmentVariable.currency);
        statement.setString(2, Sanitizer.getYearMonth(GlobalEnvironmentVariable.getDateToday()));

        ResultSet result = statement.executeQuery();
        ArrayList<WalletBalance> walletBalances = new ArrayList<>();
        while (result.next()) {
            WalletBalance walletBalance = new WalletBalance(
                    result.getInt("id"),
                    result.getString("wallet_name"),
                    result.getInt("amount"),
                    result.getString("record_date"));
            walletBalances.add(walletBalance);
        }
        return walletBalances;
    }

    public HashSet<String> getAllCategory() throws SQLException {
        HashSet<String> output = new HashSet<>();
        String sql = "SELECT DISTINCT transactions.category FROM transactions;";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();
        while (result.next()) {
            output.add(result.getString("category"));
        }
        return output;
    }

    public ArrayList<Wallet> getWalletList(String currency) throws SQLException {
        String sql = "SELECT * FROM wallets WHERE currency = ?;";

        ArrayList<Wallet> wallets = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, currency);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Wallet wallet = new Wallet(
                    result.getInt("id"),
                    result.getString("currency"),
                    result.getString("wallet_name"));
            wallets.add(wallet);
        }
        return wallets;
    }

    public void insertIntoCurrency(String currency) throws SQLException {
        System.out.println(currency);
        String sql = "INSERT INTO currency (currency_name) VALUES(?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, currency);
        statement.executeUpdate();
    }

    public ArrayList<Currency> getCurrencyList() throws SQLException {
        String sql = "SELECT * FROM currency;";

        ArrayList<Currency> currencies = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Currency currency = new Currency(result.getInt("id"), result.getString("currency_name"));
            currencies.add(currency);
        }
        return currencies;
    }

    public int insertSingleTransaction(Transaction transaction) throws SQLException {
        String table = "transactions";
        String sql = String.format(
                "INSERT INTO %s (currency, amount, category, description, trans_date, parent_id, critical, paid) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                table);

        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        Integer amount = transaction.getAmount(),
                superId = transaction.getSuperId();
        String currency = transaction.getCurrency(),
                category = transaction.getCategory(),
                description = transaction.getDescription(),
                transDate = transaction.getTransDate();
        Boolean critical = transaction.getCritical(),
                paid = transaction.getPaid();

        if (superId != null)
            statement.setInt(6, transaction.getSuperId());
        else
            statement.setNull(6, Types.INTEGER);

        statement.setString(1, currency);
        statement.setInt(2, amount);
        statement.setString(3, category);
        statement.setString(4, description);
        statement.setString(5, transDate);
        statement.setBoolean(7, critical);
        statement.setBoolean(8, paid);

        if (statement.executeUpdate() == 0) {
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

    public Transaction insertTransaction(ArrayList<Transaction> inputTransactions) throws SQLException {
        if (inputTransactions.isEmpty()) {
            throw new SQLException("Trying to input null transaction");
        }
        if (inputTransactions.size() == 1) {
            int parentId = this.insertSingleTransaction(inputTransactions.get(0));
            return this.getTransactionFromId(parentId);
        } else {
            int parentId = this.insertSingleTransaction(inputTransactions.get(0));

            for (int i = 1; i < inputTransactions.size(); i++) {
                if (inputTransactions.get(i) == null) {
                    throw new SQLException("Trying to input null transaction");
                }
                inputTransactions.get(i).setSuperId(parentId);
                this.insertSingleTransaction(inputTransactions.get(i));
            }
            return this.getTransactionFromId(parentId);
        }
    }

    public void deleteTransactionFromId(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transactions.id = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    public void deleteCurrencyFromName(String string) throws StopArgumentException, FinishArgumentException, Exception {
        String sql = "DELETE FROM currency WHERE currency_name = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, string);
        statement.executeUpdate();
    }

    public Transaction getTransactionFromId(int queryId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, queryId);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
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
