import java.util.ArrayList;

public class TransactionPrinter extends ConsoleColors {

    public static void printSingleTransaction(String colorPrefix, Transaction transaction, String prefix) {
        String printing = String.format(
                "%s, %d, %s, %s\n",
                transaction.getTransDate(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDescription());
        System.out.printf(colorPrefix + prefix + printing + RESET);
    }

    public static void printSingleTransaction(Transaction transaction) {
        printSingleTransaction("", transaction, "");
    }

    public static void printSingleTransaction(String colorPrefix, Transaction transaction) {
        printSingleTransaction(colorPrefix, transaction, "");
    }

    public static void printSingleTransaction(Transaction transaction, String prefix) {
        printSingleTransaction("", transaction, prefix);
    }

    public static void printParentChild(Transaction parentTransaction, ArrayList<Transaction> childTransactions) {
        printSingleTransaction(GREEN_BACKGROUND, parentTransaction);
        for (Transaction transaction : childTransactions) {
            printSingleTransaction(GREEN_BACKGROUND_BRIGHT, transaction, "└─");
        }
    }
}
