import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Sanitizer is an interface that will sanitize the string given into the needed
 * use. It will make sure the date is good, the name is good, the user input is
 * not a mess and will not break the application or the database.
 */
public interface Sanitizer {
    /**
     * This static method will sanitize input such as
     * 2022/06/06, 50000 | 5000 | asd, 5000
     * Into
     * 2022-06-06, 50000 | <todayDate>, 5000 | <todayDate>, 5000
     * 
     * It will make sure that the user input to be good for the database.
     * 
     * @param input
     * @return ArrayList of String that is sanitized.
     */
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

    /**
     * This static method will make sure the user input is divided into the
     * ArrayList of Array of strings
     * ex. 2022/10/20, -20000, supermarket > -10000, rice> -10000, milk
     * It will split the items from >
     * and then split again each from ,
     * 
     * @param input
     * @return ArrayList of Primitive Array of strings
     */
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

    /**
     * This static method will change the array of input into a Transaction object
     * and make it a child Transaction if parent Transaction not given.
     * It will call multiple methods in this interface to sort through and make sure
     * the string is correct.
     * ex. {"-5000", "conbini", "desc"}
     * Check if there's date or not -> {"2022/07/20", "-5000", "conbini", "desc"}
     * createTransaction() -> Transaction object with all the data
     * 
     * @param dirtyStringArray
     * @param parent
     * @return Transaction object
     */
    public static Transaction sanitizeInputArrayIntoTransaction(String[] dirtyStringArray, Transaction parent) {
        if (dirtyStringArray.length < 2) { // Check if there's only one input ex. = "-5000"
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
        /* ONLY FOR CHILD OBJECT */
        if (parent != null) {
            tempContainer.add(tempContainer.get(2));
            tempContainer.set(2, parent.getCategory());
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

    /**
     * Overload method of sanitizeInputArrayIntoTransaction that will set default
     * value of the parent as null.
     * 
     * @param dirtyStringArray
     * @return
     */
    public static Transaction sanitizeInputArrayIntoTransaction(String[] dirtyStringArray) {
        return sanitizeInputArrayIntoTransaction(dirtyStringArray, null);
    }

    /**
     * This static method will make sure that the array given will be sanitized into
     * Array List of transactions by calling methods defined in this interface.
     * ex. -5000, conbini, desc > -3000, bread
     * Call textToDirtyArray() -> {{"-5000", "conbini", "desc"}, {"-3000", "bread"}}
     * Call sanitizeInputArrayIntoTransaction() -> Transaction objects
     * And append everything into an ArrayList of Transactions
     * {{"2022/07/20", "-5000", "conbini", "desc"}, {"2022/07/20", "-3000",
     * "conbini", "bread"}}
     * 
     * @param input
     * @return ArrayList of Transaction
     * @throws Exception
     */
    public static ArrayList<Transaction> dirtyArrayToTransactionList(String input) throws Exception {
        ArrayList<String[]> dirtyArray = textToDirtyArray(input);
        ArrayList<Transaction> transactions = new ArrayList<>();

        // Parent of transaction
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

    /**
     * This static method will try and create primitive array fromt he string
     * argument according to the divider string given.
     * 
     * @param input
     * @param divider
     * @return String[]
     */
    public static String[] splitIntoArray(String input, String divider) {
        String[] output = input.split(divider);
        for (String string : output) {
            string = string.trim();
        }
        return output;
    }

    /**
     * This static method will try to cnvert date of format
     * yyyy/MM/dd -> yyyy-MM-dd
     * if it's not a valid date throw an exception.
     * 
     * @param date
     * @return string of date formatted to yyyy-MM-dd
     * @throws Exception
     */
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

    /**
     * This static method accepts clean ArrayList of transaction information and
     * construct a new Transaction object.
     * 
     * @param container
     * @param parent
     * @return Transaction object.
     */
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

    /**
     * This static method accept the string of valid date and strip the day field
     * from the date and returns it.
     * If month modifier argument was given, it will add or decrease the month.
     * 
     * yyyy-MM-dd -> yyyy-MM
     * 
     * @param date
     * @param monthModifier
     * @return String of date with format yyyy-MM
     */
    public static String getYearMonth(String date, int monthModifier) {
        SimpleDateFormat dashDate = new SimpleDateFormat("yyyy-MM-dd");
        Date convert;
        try {
            convert = dashDate.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(convert);

            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = (cal.get(Calendar.MONTH) + 1) < 10
                    ? 0 + String.valueOf(cal.get(Calendar.MONTH) + 1 + monthModifier)
                    : String.valueOf(cal.get(Calendar.MONTH) + 1 + monthModifier);
            return String.format("%s-%s", year, month);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Overload of getYearMonth() method that set default
     * value of monthModifier as 0
     * 
     * @param date
     * @return String of date with format yyyy-MM
     */
    public static String getYearMonth(String date) {
        return getYearMonth(date, 0);
    }
}
