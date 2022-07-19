import java.util.ArrayList;

public class TransactionPrinter {
    public static void printSingleTransaction(Transaction transaction, String prefix) {
        System.out.printf(
                "%s%s, %d, %s, %s\n",
                prefix,
                transaction.getTransDate(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDescription());
    }

    public static void printSingleTransaction(Transaction transaction) {
        printSingleTransaction(transaction, "");
    }

    public static void printParentChild(Transaction parentTransaction, ArrayList<Transaction> childTransactions) {
        printSingleTransaction(parentTransaction);
        for (Transaction transaction : childTransactions) {
            printSingleTransaction(transaction, "└─");
        }
    }
}
