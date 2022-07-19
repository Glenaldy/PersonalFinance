
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

    public static Transaction sanitizeInputIntoTransaction(String[] dirtyStringArray, String[] environment) {
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

        // [0]2022/07/19
        // [1]-5000
        // [2]conbini
        // [3]desc

        // [0]-5000
        // [1]conbini

        // [0]"2020/01/01"
        // [1]"-5000"
        // [2]"conbini"
        // [3]"desc"
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

        Transaction transaction = new Transaction(
                id, currency, amount, category, description, transDate, superId, critical, paid);
        return transaction;

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
