import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

public class MainMenu {
    public static void flushConsole() {
        System.out.print("\033[H\033[2J");
    }

    public static void separatorDash(Boolean newLine) {
        if (newLine)
            System.out.println("------------");
        else
            System.out.printf("------------");
    }

    public static void menuTop(String menuNameDetail) {
        flushConsole();
        System.out.printf(ConsoleColors.WHITE_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD);
        System.out.printf("MENU: %s, date: %s, currency: %s\n", menuNameDetail,
                GlobalEnvironmentVariable.getDateToday(),
                GlobalEnvironmentVariable.currency);
        System.out.println("Type \"--stop\" to quit the menu, \"--finish\" to quit the application");
        System.out.printf(ConsoleColors.RESET);
        separatorDash(true);
    }

    public static void mainMenu(Database db) {
        while (true) {
            try {
                menuTop("Main Menu");

                // SHOW CURRENT CURRENCY
                System.out.printf("Currency mode: %s\n", GlobalEnvironmentVariable.currency);

                // SHOW WALLET(S) ACCORDING TO CURRENCY
                System.out.println("Wallet in this currency: ");
                for (WalletBalance wallet : GlobalEnvironmentVariable.db.getWalletBalance(true)) {
                    System.out.printf("\t%s : %s\n", wallet.getWalletName(), wallet.getAmount());
                }

                System.out.println();

                // MENU CHOICES
                System.out.printf("%sQuit application\n", selectionColor(0));
                System.out.printf("%sInput paid transaction into the database\n", selectionColor(1));
                System.out.printf("%sInput unpaid transaction into the database\n", selectionColor(2));
                System.out.printf("%sSee my transaction for selected year-month\n", selectionColor(3));
                System.out.printf("%sChange the currency mode\n", selectionColor(4));
                System.out.printf("%sUpdate wallet balance\n", selectionColor(5));

                // GET USER INPUT
                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 0:
                        throw new FinishArgumentException("Close application");
                    case 1:
                        criticalTransactionConfirmation(true);
                        break;
                    case 2:
                        criticalTransactionConfirmation(false);
                        break;
                    case 3:
                        getCurrentMonthTransaction(db);
                        break;
                    case 4:
                        changeGlobalEnvironmentCurrency();
                        break;
                    case 5:
                        updateWalletBalance();
                        break;
                    case 6:

                        break;
                    default:
                        break;
                }
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                System.out.println("Thank you for today");
                System.exit(0);
            } catch (Exception e) {
            }
        }
    }

    public static void criticalTransactionConfirmation(Boolean paid) throws FinishArgumentException {
        while (true) {
            try {
                menuTop("Transaction Criticallity");

                System.out.printf("Select your %s transaction type\n", paid ? "Paid" : "Unpaid");
                System.out.printf("%s Quit\n", selectionColor(0));
                System.out.printf("%s Normal transaction\n", selectionColor(1));
                System.out.printf("%s Critical transaction\n", selectionColor(2));

                Integer input = Integer.parseInt(ScannerInput.scanInput().trim());
                switch (input) {
                    case 0:
                        throw new StopArgumentException("Stop called");
                    case 1:
                        inputTransactionIntoDatabase(false, paid);
                        break;
                    case 2:
                        inputTransactionIntoDatabase(true, paid);
                        break;
                    default:
                        throw new Exception();
                }

            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
    }

    public static void updateWalletBalance() throws FinishArgumentException {
        while (true) {
            try {
                flushConsole();
                System.out.printf("Currency mode: %s\n", GlobalEnvironmentVariable.currency);
                System.out.println("Select the wallet you want to change");

                ArrayList<WalletBalance> walletList = GlobalEnvironmentVariable.db.getWalletBalance(true);
                System.out.printf("%sQuit\n", selectionColor(0));
                for (int i = 0; i < walletList.size(); i++) {
                    WalletBalance wallet = walletList.get(i);
                    System.out.printf("%s%s : %s (Last updated: %s)\n", selectionColor(i + 1), wallet.getWalletName(),
                            wallet.getAmount(), wallet.getRecordDate());
                }
                System.out.printf("%sInsert new wallet\n", selectionColor(walletList.size() + 1));
                System.out.printf("%sDelete wallet\n", selectionColor(walletList.size() + 2));

                Integer input = Integer.parseInt(ScannerInput.scanInput().trim());
                if (input == 0) {
                    throw new StopArgumentException("quit");
                }
                if (input <= walletList.size()) {
                    WalletBalance wallet = walletList.get(input - 1);
                    System.out.printf("Changing the wallet %s (%s)\n", wallet.getWalletName(),
                            GlobalEnvironmentVariable.currency);
                    System.out.println("ex. 2020-10-07, 10000");
                    System.out.println("ex. 10000");
                    ArrayList<String> walletUpdate = Sanitizer.dirtyWalletInputToArray(ScannerInput.scanInput());

                    GlobalEnvironmentVariable.db.updateWalletBalance(wallet.getId(),
                            Integer.parseInt(walletUpdate.get(1)),
                            walletUpdate.get(0));
                } else if (input == walletList.size() + 1) {
                    System.out.println("Insert new wallet name");
                    String newWallet = ScannerInput.scanInput().trim();
                    GlobalEnvironmentVariable.db.insertWallet(newWallet);
                } else if (input == walletList.size() + 2) {
                    System.out.println("Select wallet number");
                    Integer deleteInput = Integer.parseInt(ScannerInput.scanInput().trim());
                    WalletBalance walletForDeletetion = walletList.get(deleteInput - 1);
                    System.out.println("delete input " + walletForDeletetion.getId());
                    System.out.println(
                            "Warning, this can't be undone. Type \"yes/y\" to continue.");
                    String confirmation = ScannerInput.scanInput().trim();
                    if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
                        GlobalEnvironmentVariable.db.deleteWallet(walletForDeletetion.getId());
                    }
                }
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
    }

    public static void getCurrentMonthTransaction(Database db) throws FinishArgumentException {
        while (true) {
            try {
                menuTop("Show transaction");

                System.out.println("Show transactions from");
                System.out.printf("%sQuit\n", selectionColor(0));
                System.out.printf("%sThis month\n", selectionColor(1));
                System.out.printf("%sLast month\n", selectionColor(2));
                System.out.printf("%sNext month\n", selectionColor(3));
                System.out.printf("%sInput my own year-month\n", selectionColor(4));

                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 0:
                        throw new StopArgumentException("Stop");
                    case 1:
                        printTransactionsByMonth(
                                Sanitizer.getYearMonth(GlobalEnvironmentVariable.getDateToday()));
                        break;
                    case 2:
                        printTransactionsByMonth(Sanitizer.getYearMonth(GlobalEnvironmentVariable.getDateToday(), -1));

                        break;
                    case 3:
                        printTransactionsByMonth(Sanitizer.getYearMonth(GlobalEnvironmentVariable.getDateToday(), 1));
                        break;
                    case 4:
                        printTransactionsByMonth(
                                String.join("-", Sanitizer.splitIntoArray(ScannerInput.scanInput(), "/")));
                        break;
                    default:
                        break;
                }
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
    }

    public static void printTransactionsByMonth(String yearMonth)
            throws StopArgumentException, FinishArgumentException, Exception {
        ArrayList<Transaction> results = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth);
        ArrayList<Transaction> criticalNotPaid = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth,
                true, false);
        ArrayList<Transaction> notCriticalnotlPaid = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth,
                false, false);
        ArrayList<Transaction> notCriticalPaid = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth,
                false, true);
        ArrayList<Transaction> criticalPaid = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth,
                true, true);

        flushConsole();
        System.out.printf("Showing transactions from %s\n", yearMonth);

        // Print wallet balances
        ArrayList<WalletBalance> earliestBalances = GlobalEnvironmentVariable.db.getWalletBalance(false);
        ArrayList<WalletBalance> latestBalances = GlobalEnvironmentVariable.db.getWalletBalance(true);
        Integer walletStart = 0, walletEnd = 0;
        for (int i = 0; i < latestBalances.size(); i++) {
            walletStart += earliestBalances.get(i).getAmount();
            walletEnd += latestBalances.get(i).getAmount();
            System.out.printf("\t%s\t|\tstart :%s, end : %s\n", latestBalances.get(i).getWalletName(),
                    earliestBalances.get(i).getAmount(), latestBalances.get(i).getAmount());
        }
        System.out.printf("Wallet Start = %s | Wallet End = %s | Wallet spending/income = %s\n", walletStart, walletEnd,
                walletEnd - walletStart);

        // Print spending and incomes
        Integer incomePaid = 0, spendingPaid = 0, incomeUnpaid = 0, spendingUnpaid = 0;
        for (Transaction transaction : results) {
            if (transaction.getAmount() >= 0 && transaction.getPaid()) {// income paid
                incomePaid += transaction.getAmount();
            } else if (transaction.getAmount() < 0 && transaction.getPaid()) { // spending paid
                spendingPaid += transaction.getAmount();
            } else if (transaction.getAmount() >= 0 && !transaction.getPaid()) {
                incomeUnpaid += transaction.getAmount();
            } else {
                spendingUnpaid += transaction.getAmount();
            }
        }
        System.out.printf("Total income in %s = %s | (%s unpaid)\n", yearMonth, incomePaid + incomeUnpaid,
                incomeUnpaid);
        System.out.printf("Total spending in %s = %s | (%s unpaid)\n", yearMonth, spendingPaid + spendingUnpaid,
                spendingUnpaid);

        System.out.printf("%sWallet end prediction this month = %s%s\n", ConsoleColors.BLUE_BACKGROUND_BRIGHT,
                walletEnd + incomeUnpaid + spendingUnpaid, ConsoleColors.RESET);

        // Tell me how much I spent in this this wallet
        // ()

        // Print critical not paid
        separatorDash(false);
        System.out.printf("Critical Unpaid, ");
        int count = 0;
        printTransactionsByField(criticalNotPaid, count);

        // Print critical paid
        separatorDash(false);
        System.out.printf("Critical Paid, ");
        count += criticalNotPaid.size();
        printTransactionsByField(criticalPaid, count);

        // Print not critical not paid
        separatorDash(false);
        System.out.printf("Not-Critical Unpaid, ");
        count += criticalPaid.size();
        printTransactionsByField(notCriticalnotlPaid, count);

        // Print not critical paid
        separatorDash(false);
        System.out.printf("Not-Critical Paid, ");
        count += notCriticalnotlPaid.size();
        printTransactionsByField(notCriticalPaid, count);
        separatorDash(false);
        separatorDash(true);

        System.out.println("Result " + results.size() + " transactions");

        separatorDash(true);
        showTransactionByCategory(results);
        separatorDash(false);
        separatorDash(true);
        System.out.printf("%sChange value\n%sCheck other transaction\n", selectionColor(0),
                selectionColor(1));
        int input = Integer.parseInt(ScannerInput.scanInput().trim());
        switch (input) {
            case 1:
                throw new Exception();
            case 0:
                System.out.println("Insert transaction to change");
                int changeInput = Integer.parseInt(ScannerInput.scanInput().trim()) - 1;
                changeTransaction(results.get(changeInput));
                break;
            default:
                break;
        }
    }

    public static void showTransactionByCategory(ArrayList<Transaction> results) {
        HashMap<String, Integer> categories = new HashMap<>();
        for (Transaction transaction : results) {
            if (categories.containsKey(transaction.getCategory())) {
                Integer temp = categories.get(transaction.getCategory());
                temp += transaction.getAmount();
                categories.put(transaction.getCategory(), temp);
            } else {
                categories.put(transaction.getCategory(), transaction.getAmount());
            }
        }
    }

    public static String selectionColor(String selection) {
        return ConsoleColors.CYAN_BACKGROUND_BRIGHT + "[ " + selection + " ]" + ConsoleColors.RESET + " ";
    }

    public static String selectionColor(int selection) {
        return selectionColor(String.format("%s", selection));
    }

    public static void printTransactionsByField(ArrayList<Transaction> transactions, int count) throws SQLException {
        System.out.println("Result " + transactions.size() + " transactions");
        Integer totalAmount = 0;
        for (int i = 0; i < transactions.size(); i++) {
            totalAmount += transactions.get(i).getAmount();
            System.out.printf(selectionColor(String.format("%d", count + 1)));
            TransactionPrinter.printParentChild(transactions.get(i),
                    GlobalEnvironmentVariable.db.getTransactionChild(transactions.get(i)));
            count++;
        }
        System.out.println("Total is " + totalAmount);
    }

    public static void changeTransaction(Transaction transaction) throws FinishArgumentException {
        while (true) {
            try {
                menuTop("Change Selected Transaction");
                Transaction newestTransaction = GlobalEnvironmentVariable.db.getTransactionFromId(transaction.getId());
                TransactionPrinter.printParentChild(newestTransaction,
                        GlobalEnvironmentVariable.db.getTransactionChild(newestTransaction));
                System.out.println("What would you like to change?");

                System.out.printf("%sCancel\n", selectionColor(0));
                System.out.printf("%sChange criticallity\n", selectionColor(1));
                System.out.printf("%sChange paid\n", selectionColor(2));
                System.out.printf("%sDelete transaction\n", selectionColor(3));

                int input = Integer.parseInt(ScannerInput.scanInput().trim());
                switch (input) {
                    case 0:
                        throw new StopArgumentException("Cancel transaction change");
                    case 1:
                        GlobalEnvironmentVariable.db.changeTransactionBooleanField(newestTransaction, "critical");
                        throw new Exception();
                    case 2:
                        GlobalEnvironmentVariable.db.changeTransactionBooleanField(newestTransaction, "paid");
                        throw new Exception();
                    case 3:
                        System.out.println(
                                "Warning, this can't be undone, it will also delete all the children transaction. Type \"yes/y\" to continue.");
                        String confirmation = ScannerInput.scanInput();
                        if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
                            GlobalEnvironmentVariable.db.deleteTransactionFromId(newestTransaction.getId());
                            break;
                        } else {
                            break;
                        }
                    default:
                        break;
                }
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
    }

    public static void changeGlobalEnvironmentCurrency() throws FinishArgumentException {
        while (true) {
            try {
                menuTop("Change Currency");

                System.out.println("SELECT TO CHANGE CURRENCY");
                System.out.printf("%sQuit\n", selectionColor(0));
                ArrayList<String> currencyList = Currency.getCurrencyList();
                for (int i = 0; i < currencyList.size(); i++) {
                    System.out.printf("%s%s\n", selectionColor(i + 1), currencyList.get(i));
                }
                System.out.printf("%sInsert new currency\n", selectionColor(currencyList.size() + 1));
                System.out.printf("%sDelete a currency\n", selectionColor(currencyList.size() + 2));

                int selection = Integer.parseInt(ScannerInput.scanInput().trim());
                if (selection == 0) {
                    throw new StopArgumentException("quit");
                }
                if (selection <= currencyList.size()) {
                    GlobalEnvironmentVariable.currency = currencyList.get(selection - 1);
                } else if (selection == currencyList.size() + 1) {
                    // Insert new currency
                    menuTop("Insert Currency");
                    System.out.printf("Insert the currency string : ");
                    GlobalEnvironmentVariable.db.insertIntoCurrency(ScannerInput.scanInput().trim());
                    throw new Exception();
                } else if (selection == currencyList.size() + 2) {
                    // Delete a currency
                    System.out.println("Select transaction to delete. Cannot delete current wallet.");
                    int deleteCurrency = Integer.parseInt(ScannerInput.scanInput().trim());
                    if (deleteCurrency <= currencyList.size() || deleteCurrency == 0) {
                        System.out.println(
                                "Warning, this can't be undone. Type \"yes/y\" to continue.");
                        String confirmation = ScannerInput.scanInput();
                        if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
                            if (!GlobalEnvironmentVariable.currency.equals(currencyList.get(deleteCurrency - 1))) {
                                GlobalEnvironmentVariable.db
                                        .deleteCurrencyFromName(currencyList.get(deleteCurrency - 1));
                            }
                            throw new Exception();
                        }
                    }
                    throw new Exception();
                }
                break;
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (Exception e) {
            }
        }
    }

    public static void inputTransactionIntoDatabase(Boolean critical, Boolean paid)
            throws FinishArgumentException {
        String optional = ConsoleColors.BLACK_BACKGROUND_BRIGHT + ConsoleColors.BLACK_BOLD;
        String mandatory = ConsoleColors.RED_BACKGROUND_BRIGHT + ConsoleColors.WHITE_BOLD;
        String reset = ConsoleColors.RESET;
        flushConsole();
        while (true) {
            try {
                menuTop((critical ? "Critical " : "Not critical") + (paid ? " Paid" : " Unpaid"));

                System.out.println("Input details sepearated by comma (,)");
                System.out.printf("%sdate%s(optional), ", optional, reset);
                System.out.printf("%samount%s(mandatory), ", mandatory, reset);
                System.out.printf("%scategory%s(mandatory), ", mandatory, reset);
                System.out.printf("%sdescription%s(optional) \n", optional, reset);

                System.out.printf("%sdate%s(optional), ", optional, reset);
                System.out.printf("%samount%s(mandatory), ", mandatory, reset);
                System.out.printf("%scategory%s(mandatory), ", mandatory, reset);
                System.out.printf("%sdescription%s(optional) ", optional, reset);
                System.out.printf("> %schild-transaction-amount%s(mandatory), ", mandatory, reset);
                System.out.printf("%schild-description%s(mandatory)\n", mandatory, reset);

                System.out.printf("ex. \t%s2022-10-20%s, ", optional, reset);
                System.out.printf("%s-20000%s, ", mandatory, reset);
                System.out.printf("%ssupermarket%s, ", mandatory, reset);
                System.out.printf("%srice and milk%s\n", optional, reset);

                System.out.printf("ex. \t%s2022/10/20%s, ", optional, reset);
                System.out.printf("%s-20000%s, ", mandatory, reset);
                System.out.printf("%ssupermarket%s ", mandatory, reset);
                System.out.printf("> %s-10000%s, ", mandatory, reset);
                System.out.printf("%srice%s", mandatory, reset);
                System.out.printf("> %s-10000%s, ", mandatory, reset);
                System.out.printf("%smilk%s\n", mandatory, reset);

                System.out.printf("ex. \t%s-20000%s, ", mandatory, reset);
                System.out.printf("%ssupermarket%s\n", mandatory, reset);

                System.out.printf("Lists of category in the database");
                GlobalEnvironmentVariable.db.getAllCategory().forEach(value -> {
                    System.out.printf(" | %s", value);
                });
                System.out.println("\nInsert your own or choose from the list");

                System.out.printf("\nInsert your transaction here :\t");

                ArrayList<Transaction> results = ScannerInput.getInputTransaction(GlobalEnvironmentVariable.db);
                results.forEach(each -> {
                    each.setCritical(critical);
                    each.setPaid(paid);
                });
                Transaction result = GlobalEnvironmentVariable.db.insertTransaction(results);
                TransactionPrinter.printParentChild(result,
                        GlobalEnvironmentVariable.db.getTransactionChild(result));
                System.out.println("Transaction inserted, press any key to continue");
                ScannerInput.scanInput();
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (SQLException e) {
            } catch (Exception e) {
            }
        }
    }
}