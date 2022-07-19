package helper;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Database {
    Connection connection;
    Statement statement;

    public Database(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public void selectFrom(String table) throws SQLException {
        String sql = String.format("SELECT * FROM %s;", table);
        ResultSet result = statement.executeQuery(sql);
        ResultSetMetaData metadata = result.getMetaData();
        int columnCount = metadata.getColumnCount();
        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metadata.getColumnName(i);
                System.out.println(columnName + result.getObject(i));
            }
            // Integer id = result.getInt("id");
            // Integer amount = result.getInt("amount");
            // String category = result.getString("category");
            // String description = result.getString("description");
            // String date = result.getString("transdate");

            // Transaction tran = new Transaction(amount, category, description, date);

            // System.out.println(tran.amount);
        }

    }

}
