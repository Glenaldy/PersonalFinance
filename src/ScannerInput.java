
import java.util.ArrayList;
import java.util.Scanner;

public class ScannerInput {
    static Scanner scannerObj = new Scanner(System.in);

    public static String[] scanTransaction() {
        String[] input;
        while (true) {
            try {
                input = scannerObj.nextLine().split(",");
                sanitizeInput();
                break;
            } catch (Exception e) {
                System.out.printf(
                        "Invalid input.\n%s\n");
                scannerObj.nextLine();
            }
        }
        return input;
    }

    public static ArrayList<Transaction> parentChildInput(String[][] parentChild, String[] environment) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction parent = sanitizeInputIntoTransaction(parentChild[0], environment);
        transactions.add(parent);
        for (int i = 1; i < parentChild.length; i++) {
            transactions.add(sanitizeInputIntoTransaction(parentChild[i], environment, parent));
        }
        return transactions;
    }

    public static Transaction sanitizeInputIntoTransaction(String[] dirtyStringArray, String[] environment,
            Transaction parent) {
        String keyWord = "--hint";

        Integer id = null;
        String currency = environment[1];
        Integer amount = null; //
        String category = null; //
        String description = null; //
        String transDate = environment[0];//
        Integer superId = null; //
        Boolean critical = false; //
        Boolean paid = true; //

        if (dirtyStringArray[0].toLowerCase() == keyWord) {
            ;
        }

        try {
            amount = Integer.parseInt(dirtyStringArray[0]);
            category = dirtyStringArray[1];
            try {
                description = dirtyStringArray[2];
            } catch (Exception e) {
                ;
            }
        } catch (Exception e) {
            transDate = dirtyStringArray[0];
            amount = Integer.parseInt(dirtyStringArray[1]);
            category = dirtyStringArray[2];
            try {
                description = dirtyStringArray[3];
            } catch (Exception f) {
                ;
            }
        }

        try {
            amount = Integer.parseInt(dirtyStringArray[0]);
        } catch (Exception e) {
            amount = Integer.parseInt(dirtyStringArray[1]);
        }

        if (parent != null) {
            category = parent.category;
            description = dirtyStringArray[1];
            transDate = parent.transDate;
        }

        Transaction transaction = new Transaction(
                id, currency, amount, category, description, transDate, superId, critical, paid);
        return transaction;

    }

    public static Transaction sanitizeInputIntoTransaction(String[] dirtyStringArray,
            String[] environment) {
        return sanitizeInputIntoTransaction(dirtyStringArray, environment, null);
    }

    public static String[] scanTransactionParent() {
        String[] input;
        while (true) {
            try {
                input = scannerObj.nextLine().split(">");
                break;
            } catch (Exception e) {
                System.out.printf(
                        "Invalid input.\n%s\n");
                scannerObj.nextLine();
            }
        }
        for (String string : input) {
            string.split(",")
        }
        return input;
    }

    private String[] splitByComma(String input) {
        return input.split(",")
    }
}
