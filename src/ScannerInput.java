import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

public class ScannerInput {
    private static Scanner scannerObj = new Scanner(System.in);
    private final static String[] KEYWORD = { "--stop", "--finish" };

    public static ArrayList<Transaction> getInputTransaction(Database db) throws Exception {
        while (true) {
            try {
                String userInput = (ScannerInput.scanInput());
                ArrayList<Transaction> result = Sanitizer.dirtyArrayToTransactionList(userInput);
                return result;
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public static String scanInput() throws StopArgumentException, FinishArgumentException, Exception {
        String input;
        while (true) {
            try {
                input = scannerObj.nextLine();
                boolean checkKeyword = Arrays.stream(KEYWORD).anyMatch(input.trim()::equalsIgnoreCase);
                if (checkKeyword) {
                    if (input.trim().equals(KEYWORD[0]))
                        throw new StopArgumentException("Stopping input");
                    else if (input.trim().equals(KEYWORD[1]))
                        throw new FinishArgumentException("Closing application, thank you");
                }
                break;
            } catch (StopArgumentException e) {
                throw e;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
                System.out.printf("Invalid input.\n%s\n");
                scannerObj.nextLine();
            }
        }
        return input;
    }

    public static ArrayList<Transaction> parentChildInput(String[][] parentChild) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction parent = Sanitizer.sanitizeInputArrayIntoTransaction(parentChild[0]);
        transactions.add(parent);
        for (int i = 1; i < parentChild.length; i++) {

            transactions.add(Sanitizer.sanitizeInputArrayIntoTransaction(parentChild[i]));
        }
        return transactions;
    }

}