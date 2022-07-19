
import java.util.Scanner;

public class ScannerInput {
    static Scanner scannerObj = new Scanner(System.in);

    public static String[] ScanTransaction() {
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
}
