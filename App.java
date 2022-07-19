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
        // ScannerInput.scanTransaction()
        // yen, -5000, conbini, bought this today, 2022/07/19
        // 2022/07/19, -5000, conbini, desc
        // int i = db.insertIntoTransaction(test1);
        // System.out.println("TEST 1\t Inserting into id " + i);
        Transaction transactionA = db.insertTransaction(ScannerInput.sanitizeInputIntoTransaction(test1, environment));

        System.out.println("Insert successful");
        System.out.println(transactionA.transDate + transactionA.amount +
                transactionA.category
                + transactionA.description + transactionA.superId + transactionA.critical);

        // INSERT INTO PARENT CHILD
        // yen, -5000, conbini, desc, 2022/07/19 > -3000, bread > -2000, milk
        // String[] test2 = ScannerInput.scanTransactionParent();
        // for (String string : test2) {
        // System.out.println(string);
        // }

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
