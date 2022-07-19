import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        String[] environment = { DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()).toString(), "yen" };

        final String PATH = "D:/School/1_KCGI/2022 Spring/Object Oriented Programming/Programming/jp/kcgi/twentytwo/spring/oop/PersonalFinance";
        String jdbcUrl = "jdbc:sqlite:";
        String database = "database/personalfinance.db";

        String databaseURL = jdbcUrl + "/" + PATH + "/" + database;

        Database db = new Database(databaseURL, PATH);

        // INSERT INTO SINGLE
        String[] test1 = { "-5000", "conbini" };

        // { "2020/01/01", "-5000", "conbini", "bought this today" }
        // int i = db.insertIntoTransaction(test1);
        Transaction transactionA = db.insertTransaction(ScannerInput.sanitizeInputIntoTransaction(test1, environment));

        System.out.println("Insert successful");
        System.out.println(transactionA.transDate + transactionA.amount +
                transactionA.category
                + transactionA.description + transactionA.superId + transactionA.critical);

        String[][] test2 = { { "-5000", "conbini", "desc" }, { "-3000", "bread" }, { "-2000", "milk" } };

        ArrayList<Transaction> set = ScannerInput.parentChildInput(test2, environment);
        Transaction insertTest2 = db.insertTransaction(set.get(0));
        System.out.println(insertTest2.id);
        for (int i = 1; i < set.size(); i++) {
            set.get(i).superId = insertTest2.id;
            db.insertTransaction(set.get(i));
        }
        TransactionPrinter.printParentChild(insertTest2, db.getTransactionChild(insertTest2));
        System.out.println("SUCCESS");

        // GET TRANSACTION FROM ID = Transaction
        Transaction testTran1 = db.getTransactionFromId(2);
        System.out.println("TEST 2\t Selecting from id " + testTran1.getId());

        // COUNT CHILD
        Integer countChildTran1 = db.checkHasChild(testTran1);
        System.out.println("TEST 3\t Number of child of id " + testTran1.getId() + " is " + countChildTran1);

        // GET ARRAY OF CHILD TRANSACTION FROM TRANSACTION
        ArrayList<Transaction> childSet = db.getTransactionChild(testTran1);
        System.out.println("TEST 4\t Child of id " + testTran1.getId());
        for (Transaction transaction : childSet) {
            System.out.printf("%d ", transaction.getId());
        }
        System.out.println();

        // Print single transaction
        TransactionPrinter.printSingleTransaction(testTran1);

        // Print parent transaction and their child
        TransactionPrinter.printParentChild(testTran1, db.getTransactionChild(testTran1));

    }
}
