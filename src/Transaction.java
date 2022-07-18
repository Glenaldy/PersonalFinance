import java.util.Scanner;

class Transaction {
    String category,
            description,
            date;
    Integer amount;

    public Transaction(Integer amount, String category, String description, String date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        // if (transaction[3].length() == 0) {
        // this.date =
        // LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // }

        // System.out.printf("%d %s %s\n", this.amount, this.category, this.date);
    }

    public String[] getTransaction() {
        return new String[] { Integer.toString(amount), category, description, date };
    }

    private String[] scanTransaction() {
        // ArrayList<String> strings = new ArrayList<String>(Collections.nCopies(4,
        // ""));
        String[] strings = { "", "", "", "" };
        Scanner scannerObj = new Scanner(System.in);

        String message = "Enter new transaction separated by comma:\nFormat <Debit/Credit, Category, Description (optional), Date(optional)>\n-ex: 5000, Supermarket, Description, 07/10/2022 | 5000, Supermarket | 5000, Supermarket, , 07/10/2022";

        System.out.println(message);

        while (true) {
            try {
                String[] input = scannerObj.nextLine().split(",");
                if (!(input.length < 2 || input.length > 4) && !isInteger(input[0].trim())) {
                    throw new IllegalArgumentException();
                }

                for (int i = 0; i < input.length; i++) {
                    strings[i] = input[i].trim();
                }
                break;
            } catch (Exception e) {
                System.out.printf("Invalid input.\n%s\n", message);
            }
        }
        scannerObj.close();
        return strings;
    }

    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
