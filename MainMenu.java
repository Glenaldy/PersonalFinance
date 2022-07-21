import java.sql.SQLException;
import java.util.ArrayList;

import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

public class MainMenu {
    public static void flushConsole() {
        System.out.print("\033[H\033[2J");
    }

    public static void separatorDash() {
        System.out.println("------------");
    }

    public static void menuTop(String menuNameDetail) {
        flushConsole();
        System.out.printf("MENU: %s, date: %s, currency: %s\n", menuNameDetail,
                GlobalEnvironmentVariable.getDateToday(),
                GlobalEnvironmentVariable.currency);
        System.out.println("Type \"--stop\" to quit the menu, \"--finish\" to quit the application");
        separatorDash();
    }

    public static void mainMenu(Database db) {
        while (true) {
            try {
                menuTop("Main Menu");

                // SHOW CURRENT CURRENCY
                System.out.printf("Currency mode: %s\n", GlobalEnvironmentVariable.currency);

                // SHOW WALLET(S) ACCORDING TO CURRENCY
                System.out.println("Wallet in this currency: ");
                for (WalletBalance wallet : GlobalEnvironmentVariable.db.getLatestWalletBalance()) {
                    System.out.printf("\t%s : %s\n", wallet.getWalletName(), wallet.getLatestAmount());
                }

                System.out.println();

                // MENU CHOICES
                System.out.printf("[1] Input paid transaction into the database\n");
                System.out.printf("[2] Input unpaid transaction into the database\n");
                System.out.printf("[3] See my transaction for selected year-month\n");
                System.out.printf("[4] \n");
                System.out.printf("[5] Change the currency mode\n");
                System.out.printf("[6] Update wallet balance\n");
                System.out.printf("[7] \n");

                // GET USER INPUT
                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
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
                        // TODO: CHANGE TRANSACTION
                        break;
                    case 5:
                        changeGlobalEnvironmentCurrency();
                        break;
                    case 6:
                        updateWalletBalance();
                        break;
                    case 7:

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
                menuTop("Transaction Critiallity");

                System.out.printf("Select your %s transaction type\n", paid ? "Paid" : "Unpaid");

                System.out.printf("[1] Normal transaction\n");
                System.out.printf("[2] Critical transaction\n");

                Integer input = Integer.parseInt(ScannerInput.scanInput().trim());
                switch (input) {
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

                ArrayList<WalletBalance> walletList = GlobalEnvironmentVariable.db.getLatestWalletBalance();
                for (int i = 0; i < walletList.size(); i++) {
                    WalletBalance wallet = walletList.get(i);
                    System.out.printf("[%d] %s : %s (Last updated: %s)\n", i + 1, wallet.getWalletName(),
                            wallet.getLatestAmount(), wallet.getLatestRecord());
                }

                Integer input = Integer.parseInt(ScannerInput.scanInput().trim());
                if (input <= walletList.size()) {
                    WalletBalance wallet = walletList.get(input - 1);
                    System.out.printf("Changing the wallet %s (%s)\n", wallet.getWalletName(),
                            GlobalEnvironmentVariable.currency);

                    ArrayList<String> walletUpdate = Sanitizer.dirtyWalletInputToArray(ScannerInput.scanInput());

                    GlobalEnvironmentVariable.db.updateWalletBalance(wallet.getId(),
                            Integer.parseInt(walletUpdate.get(1)),
                            walletUpdate.get(0));
                    ScannerInput.scanInput();
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
                System.out.printf("[0] Quit\n");
                System.out.printf("[1] This month\n");
                System.out.printf("[2] Last month\n");
                System.out.printf("[3] Next month\n");
                System.out.printf("[4] Input my own year-month\n");

                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 0:
                        throw new StopArgumentException("Stop from menu 2");
                    case 1:
                        printTransactionsByMonth(
                                GlobalEnvironmentVariable.getYearMonth(GlobalEnvironmentVariable.getDateToday()));
                        break;
                    case 2:
                        printTransactionsByMonth(GlobalEnvironmentVariable
                                .getYearMonth(GlobalEnvironmentVariable.getDateToday(), -1));

                        break;
                    case 3:
                        printTransactionsByMonth(GlobalEnvironmentVariable
                                .getYearMonth(GlobalEnvironmentVariable.getDateToday(), 1));
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
        System.out.printf("Showing transactions from \n", yearMonth);
        ArrayList<Transaction> results;
        results = GlobalEnvironmentVariable.db.getTransactionFromYearMonth(yearMonth);
        for (int i = 0; i < results.size(); i++) {
            System.out.printf("[%d]\n", i + 1);
            TransactionPrinter.printParentChild(results.get(i),
                    GlobalEnvironmentVariable.db.getTransactionChild(results.get(i)));
        }
        System.out.println();
        System.out.println("Result " + results.size() + " transactions");
        separatorDash();
        System.out.println("[0] Change value [1] Check other transaction");
        int input = Integer.parseInt(ScannerInput.scanInput().trim());
        switch (input) {
            case 0:
                System.out.println("Insert transaction to change");
                int changeInput = Integer.parseInt(ScannerInput.scanInput().trim()) - 1;

                changeTransaction(results.get(changeInput));

                break;
            case 1:
                throw new Exception();
            default:
                break;
        }

    }

    public static void changeTransaction(Transaction transaction) throws FinishArgumentException {
        while (true) {
            try {
                menuTop("Change Selected Transaction");
                Transaction newestTransaction = GlobalEnvironmentVariable.db.getTransactionFromId(transaction.getId());
                TransactionPrinter.printParentChild(newestTransaction,
                        GlobalEnvironmentVariable.db.getTransactionChild(newestTransaction));
                System.out.println("What would you like to change?");

                System.out.printf("[0] Cancel\n");
                System.out.printf("[1] Change criticallity\n");
                System.out.printf("[2] Change paid\n");
                System.out.printf("[3] Delete transaction\n");

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
                            System.out.println(newestTransaction.getId());
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
                System.out.printf("[0] Quit\n");
                ArrayList<String> currencyList = Currency.getCurrencyList();
                for (int i = 0; i < currencyList.size(); i++) {
                    System.out.printf("[%d] %s\n", i + 1, currencyList.get(i));
                }
                System.out.printf("[%s] Insert new currency\n", currencyList.size());

                int selection = Integer.parseInt(ScannerInput.scanInput().trim());
                if (selection == 0) {
                    throw new StopArgumentException("quit");
                }
                if (selection <= currencyList.size()) {
                    GlobalEnvironmentVariable.currency = currencyList.get(selection - 1);
                } else {
                    flushConsole();
                    System.out.println("Input string of your currency");
                    GlobalEnvironmentVariable.db.insertIntoCurrency(ScannerInput.scanInput());
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
                menuTop((critical ? "critical " : "not critical") + (paid ? "Paid" : "Unpaid"));

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

                System.out.printf("\nTransaction:\t");

                Transaction result = ScannerInput.getInputTransactionPushDB(GlobalEnvironmentVariable.db);
                result.setCritical(critical);
                result.setPaid(paid);

                TransactionPrinter.printParentChild(result, GlobalEnvironmentVariable.db.getTransactionChild(result));
                System.out.println();
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