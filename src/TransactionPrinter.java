import java.util.ArrayList;

/**
 * TransactionPrinter interface allows the ease of printing Transaction objects
 * with all their children.
 */
public interface TransactionPrinter {

    /**
     * This static method will print the transaction object given
     * It will add colors and add criticallity or unpaid field according to the
     * value.
     * 
     * @param colorPrefix
     * @param transaction
     * @param prefix
     */
    public static void printSingleTransaction(String colorPrefix, Transaction transaction, String prefix) {
        String critical = transaction.getCritical() ? ", critical" : ", not critical";
        String paid = transaction.getPaid() ? "" : ", unpaid";
        if (transaction.getSuperId() != 0) {
            critical = "";
            paid = "";
        }

        String printing = String.format(
                "%s, %d, %s, %s%s%s\n",
                transaction.getTransDate(),
                transaction.getAmount(),
                transaction.getCategory(),
                transaction.getDescription(),
                critical, paid);
        System.out.printf(colorPrefix + prefix + printing + ConsoleColors.RESET);
    }

    /**
     * Overload of printSingleTransaction() that set default value of colorPrefix
     * and prefix as null
     * 
     * @param transaction
     */
    public static void printSingleTransaction(Transaction transaction) {
        printSingleTransaction("", transaction, "");
    }

    /**
     * Overload of printSingleTransaction() that set default value of prefix
     * as null
     * 
     * @param colorPrefix
     * @param transaction
     */
    public static void printSingleTransaction(String colorPrefix, Transaction transaction) {
        printSingleTransaction(colorPrefix, transaction, "");
    }

    /**
     * Overload of printSingleTransaction() that set default value of colorPrefix
     * as null
     * 
     * @param transaction
     * @param prefix
     */
    public static void printSingleTransaction(Transaction transaction, String prefix) {
        printSingleTransaction("", transaction, prefix);
    }

    /**
     * This static method will print out the parentTransaction and the ArrayList of
     * the childTransaction by calling the printSingleTransaction() method and gives
     * it different colors for either parent or children.
     * 
     * @param parentTransaction
     * @param childTransactions
     */
    public static void printParentChild(Transaction parentTransaction, ArrayList<Transaction> childTransactions) {
        printSingleTransaction(ConsoleColors.GREEN_BACKGROUND, parentTransaction);

        Integer uncounted = 0;
        for (Transaction transaction : childTransactions) {
            uncounted += transaction.getAmount();
        }
        uncounted = parentTransaction.getAmount() - uncounted;
        String color = (uncounted == 0 && childTransactions.size() > 0) ? ConsoleColors.GREEN_BACKGROUND
                : ConsoleColors.RED_BACKGROUND;

        if (childTransactions.size() > 0) {
            System.out.println(color + "├─" + "Uncounted: " + uncounted + ConsoleColors.RESET);
        }

        for (Transaction transaction : childTransactions) {
            printSingleTransaction(ConsoleColors.GREEN_BACKGROUND_BRIGHT, transaction, "└─");
        }
    }
}
