import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Sanitizer {
    public static ArrayList<String> dirtyWalletInputToArray(String input) {
        // 2022-06-06, 50000
        String[] arrayInput = splitIntoArray(input, ",");
        ArrayList<String> output = new ArrayList<>();
        try {
            /* CHECK IF [0] NOT STRING */
            Integer.parseInt(arrayInput[0]);
            output.add("");
        } catch (Exception e) {
            /* IF [0] IS STRING DO NOTHING */
        }

        for (String string : arrayInput) {
            output.add(string);
        }

        /* VALIDATE DATE */
        try {
            output.set(0, convertDate(output.get(0)));
        } catch (Exception e) {
            output.set(0, GlobalEnvironmentVariable.getDateToday());
        }
        return output;
    }

    public static ArrayList<Transaction> dirtyArrayToTransactionList(String input) throws Exception {
        // -5000, conbini, desc > -3000, bread
        ArrayList<String[]> dirtyArray = textToDirtyArray(input);
        // {{"-5000", "conbini", "desc"}, {"-3000", "bread"}}
        ArrayList<Transaction> transactions = new ArrayList<>();

        // Parent of transaction {"-5000", "conbini", "desc"}
        Transaction parent = sanitizeInputArrayIntoTransaction(dirtyArray.get(0));
        if (parent == null) {
            throw new Exception();
        } else {
            transactions.add(parent);
        }

        // Child of transaction {"-3000", "bread"}
        for (int i = 1; i < dirtyArray.size(); i++) {
            if (dirtyArray.get(i) != null) {
                Transaction child = sanitizeInputArrayIntoTransaction(dirtyArray.get(i), parent);
                if (child == null) {
                    throw new Exception();
                }
                transactions.add(child);
            }
        }
        return transactions;
    }

    public static ArrayList<String[]> textToDirtyArray(String input) {
        ArrayList<String[]> output = new ArrayList<>();
        for (String string : splitIntoArray(input, ">")) {
            String[] array = splitIntoArray(string, ",");
            for (int i = 0; i < array.length; i++)
                array[i] = array[i].trim();
            output.add(array);
        }
        return output;
    }

    public static String[] splitIntoArray(String input, String divider) {
        String[] output = input.split(divider);
        for (String string : output) {
            string = string.trim();
        }
        return output;
    }

    public static Transaction sanitizeInputArrayIntoTransaction(String[] dirtyStringArray, Transaction parent) {
        // ex. {"-5000", "conbini", "desc"}
        if (dirtyStringArray.length < 2) {
            // Check if there's only one input ex. = "-5000"
            return null;
        }

        ArrayList<String> tempContainer = new ArrayList<>();
        /* CHECK IF DATE STRING IS IN THE INPUT */
        try {
            /* CHECK IF [0] NOT STRING */
            Integer.parseInt(dirtyStringArray[0]);
            tempContainer.add("");
        } catch (Exception e) {
            /* IF [0] IS STRING DO NOTHING */
        }

        /* ADD EVERYTHING INTO THE temp */
        for (String string : dirtyStringArray) {
            tempContainer.add(string);
        }
        // ex. {"2022/07/20", "5000", "job1"}
        /* ONLY FOR CHILD OBJECT */
        if (parent != null) {
            tempContainer.add(tempContainer.get(2));
            tempContainer.set(2, parent.category);
        }

        /* VALIDATE DATE */
        try {
            tempContainer.set(0, convertDate(tempContainer.get(0)));
        } catch (Exception e) {
            tempContainer.set(0, GlobalEnvironmentVariable.getDateToday());
        }

        // ex. {"2022/07/20", "-5000", "conbini", "desc"}

        Transaction output = createTransaction(tempContainer, parent);
        return output;
    }

    public static String convertDate(String date) throws Exception {
        SimpleDateFormat slashDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dashDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convert = slashDate.parse(date);
            String converted = dashDate.format(convert);
            return converted;
        } catch (Exception e) {
        }

        try {
            Date convert = dashDate.parse(date);
            String converted = dashDate.format(convert);
            return converted;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Transaction sanitizeInputArrayIntoTransaction(String[] dirtyStringArray) {
        return sanitizeInputArrayIntoTransaction(dirtyStringArray, null);
    }

    public static Transaction createTransaction(ArrayList<String> container, Transaction parent) {
        String currency = GlobalEnvironmentVariable.currency;
        Integer id = null;
        Integer amount = null;
        String category = null;
        String description = null;
        String transDate = container.get(0);
        Boolean critical = false;
        Boolean paid = true;
        Integer superId = null;

        /* Mandatory value */
        amount = Integer.parseInt(container.get(1)); // -5000
        category = container.get(2); // conbini

        /* Not Mandatory value (can be null) */
        /* DESCRIPTION CHECK */
        try {
            // Check if there's description
            description = container.get(3);
        } catch (Exception e) {
            ; // Let it be null if there's none
        }

        Transaction transaction = new Transaction(id, currency, amount, category, description, transDate, superId,
                critical, paid);
        return transaction;
    }
}
