import java.util.ArrayList;

public interface TransactionPrinter {

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
