import java.util.ArrayList;

public class Sanitizer {
    public static ArrayList<Transaction> dirtyArrayToTransactionList(String input) {
        ArrayList<String[]> dirtyArray = textToDirtyArray(input);
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (String[] transaction : dirtyArray) {
            transactions.add(sanitizeInputIntoTransaction(transaction));
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

    public static Transaction sanitizeInputIntoTransaction(String[] dirtyStringArray) {
        ArrayList<String> tempContainer = new ArrayList<>();
        try {
            Integer.parseInt(dirtyStringArray[0]);
            tempContainer.add("");
            for (String string : dirtyStringArray) {
                tempContainer.add(string);
            }
        } catch (Exception e) {
            for (String string : dirtyStringArray) {
                tempContainer.add(string);
            }
        }
        Transaction output = createTransaction(tempContainer);

        return output;
    }

    public static Transaction createTransaction(ArrayList<String> container) {
        Integer id = null;
        String currency = GlobalEnvironmentVariable.currency;
        Integer amount = null;
        String category = null;
        String description = null;
        String transDate = container.get(0);
        Integer superId = null;
        Boolean critical = false;
        Boolean paid = true;

        /* Mandatory value */
        amount = Integer.parseInt(container.get(1));
        category = container.get(2);

        /* Not Mandatory value (can be null) */
        transDate = (transDate == null) ? container.get(0) : GlobalEnvironmentVariable.getDateToday();
        try {
            description = container.get(3);
        } catch (Exception e) {
            ;
        }

        Transaction transaction = new Transaction(id, currency, amount, category, description, transDate, superId,
                critical, paid);

        return transaction;
    }
}
