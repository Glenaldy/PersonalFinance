import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

public class Menu {
    public static void flushConsole() {
        System.out.print("\033[H\033[2J");
    }

    public static void mainMenu(Database db) {
        while (true) {
            try {
                System.out.print("\033[H\033[2J");
                System.out.println(GlobalEnvironmentVariable.getDateToday());
                // SHOW CURRENT CURRENCY
                System.out.printf("Currency mode: %s\n", GlobalEnvironmentVariable.currency);
                // SHOW WALLET(S) ACCORDING TO CURRENCY
                System.out.println("Wallet in this currency: " +
                        Wallet.walletList().stream().map(Wallet::getWalletName).collect(Collectors.joining(", ")));
                System.out.println();

                // MENU CHOICES
                System.out.printf("[1] Input transaction into the database\n");
                System.out.printf("[2] See my transaction for selected year-month\n");
                System.out.printf("[3] Change my currency mode\n");
                System.out.printf("[4]\n");
                System.out.printf("[5]\n");
                System.out.printf("[6]\n");
                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 1:
                        inputTransactionIntoDatabase(db);
                        break;
                    case 2:
                        getCurrentMonthTransaction(db);
                        break;
                    case 3:
                        changeGlobalEnvironmentCurrency();
                        break;
                    case 4:
                        break;
                    case 5:
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

    public static void getCurrentMonthTransaction(Database db) throws FinishArgumentException {
        while (true) {
            try {
                flushConsole();
                System.out.println("MENU: TRANSACTION SHOW");
                System.out.println(GlobalEnvironmentVariable.getDateToday());
                System.out.println("Transactions from:");
                System.out.printf("[0] Input my own year-month\n");
                System.out.printf("[1] This month\n");
                System.out.printf("[2] Last month\n");
                System.out.printf("[3] Next month\n");

                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 0:

                        break;
                    case 1:
                        String yearMonth = GlobalEnvironmentVariable
                                .getYearMonth(GlobalEnvironmentVariable.getDateToday());
                        System.out.println(yearMonth);

                        ArrayList<Transaction> results = GlobalEnvironmentVariable.db
                                .getTransactionFromYearMonth(yearMonth);

                        for (Transaction transaction : results) {
                            TransactionPrinter.printParentChild(transaction,
                                    GlobalEnvironmentVariable.db.getTransactionChild(transaction));
                        }

                        String asd = ScannerInput.scanInput();
                        break;
                    case 2:

                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (SQLException e) {
            } catch (Exception e) {
                System.out.println(
                        "Invalid input, refer to the syntax\n\"--stop\" to stop Inputting\n\"--finish\" to end the program");
            }
        }
    }

    public static void changeGlobalEnvironmentCurrency() throws FinishArgumentException {
        while (true) {
            try {
                flushConsole();
                System.out.println("YOU ARE NOW IN THE CURRENCY CHANGE MENU");
                System.out.println("SELECT TO CHANGE CURRENCY");

                ArrayList<String> currencyList = Currency.getCurrencyList();
                System.out.printf("[0] Insert new currency\n");
                for (int i = 0; i < currencyList.size(); i++) {
                    System.out.printf("[%d] %s\n", i + 1, currencyList.get(i));
                }

                String selection = ScannerInput.scanInput();
                if (Integer.parseInt(selection) <= currencyList.size()) {
                    GlobalEnvironmentVariable.currency = currencyList.get(Integer.parseInt(selection) - 1);
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

    public static void inputTransactionIntoDatabase(Database db) throws FinishArgumentException {
        while (true) {
            try {
                flushConsole();
                System.out.println("YOU ARE NOW IN THE TRANSACTION INPUT MENU");
                Transaction result = ScannerInput.getInputTransactionPushDB(db);
                if (result != null) {
                    TransactionPrinter.printParentChild(result, db.getTransactionChild(result));
                } else
                    throw new Exception();
            } catch (StopArgumentException e) {
                break;
            } catch (FinishArgumentException e) {
                throw e;
            } catch (SQLException e) {
            } catch (Exception e) {
                System.out.println(
                        "Invalid input, refer to the syntax\n\"--stop\" to stop Inputting\n\"--finish\" to end the program");
            }
        }
    }
}