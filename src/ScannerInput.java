import java.util.ArrayList;
import java.util.Scanner;

public class ScannerInput extends Sanitizer {
    static Scanner scannerObj = new Scanner(System.in);

    public static String[] scanTransaction() {
        String[] input;
        while (true) {
            try {
                input = scannerObj.nextLine().split(",");
                break;
            } catch (Exception e) {
                System.out.printf(
                        "Invalid input.\n%s\n");
                scannerObj.nextLine();
            }
        }
        return input;
    }

    public static ArrayList<Transaction> parentChildInput(String[][] parentChild) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction parent = sanitizeInputIntoTransaction(parentChild[0]);
        transactions.add(parent);
        for (int i = 1; i < parentChild.length; i++) {
            transactions.add(sanitizeInputIntoTransaction(parentChild[i]));
        }
        return transactions;
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
            string.split(",");
        }
        return input;
    }
}