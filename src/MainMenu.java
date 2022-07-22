import CustomException.FinishArgumentException;
import CustomException.StopArgumentException;

/**
 * Main menu interface controls all the functionality of the command line
 * interface for the user by defining all the selection the user can do or not
 */
public interface MainMenu extends Menus {

    /**
     * This static method is the main menu where all of the interface in the comman
     * line is controlled by this method.
     * 
     * This will create these selection with this method.
     * [ 0 ] Quit application
     * [ 1 ] Input paid transaction into the database
     * [ 2 ] Input unpaid transaction into the database
     * [ 3 ] See my transaction for selected year-month
     * [ 4 ] Change the currency mode
     * [ 5 ] Update wallet balance
     * 
     * It also at any time accepts the input --stop and --finish to stop the
     * program.
     * 
     * @param db
     */
    public static void mainMenu(Database db) {
        while (true) {
            try {
                Menus.menuTop("Main Menu");

                // SHOW CURRENT CURRENCY
                System.out.printf("Currency mode: %s\n", GlobalEnvironmentVariable.currency);

                // SHOW WALLET(S) ACCORDING TO CURRENCY
                System.out.println("Wallet in this currency: ");
                for (WalletBalance wallet : GlobalEnvironmentVariable.db.getWalletBalance(true)) {
                    System.out.printf("\t%s : %s\n", wallet.getWalletName(), wallet.getAmount());
                }

                System.out.println();

                // MENU CHOICES
                System.out.printf("%sQuit application\n", Menus.selectionColor(0));
                System.out.printf("%sInput paid transaction into the database\n", Menus.selectionColor(1));
                System.out.printf("%sInput unpaid transaction into the database\n", Menus.selectionColor(2));
                System.out.printf("%sSee my transaction for selected year-month\n", Menus.selectionColor(3));
                System.out.printf("%sChange the currency mode\n", Menus.selectionColor(4));
                System.out.printf("%sUpdate wallet balance\n", Menus.selectionColor(5));

                // GET USER INPUT
                String input = ScannerInput.scanInput();
                switch (Integer.parseInt(input.trim())) {
                    case 0:
                        throw new FinishArgumentException("Close application");
                    case 1:
                        Menus.criticalTransactionConfirmation(true);
                        break;
                    case 2:
                        Menus.criticalTransactionConfirmation(false);
                        break;
                    case 3:
                        Menus.getCurrentMonthTransaction(db);
                        break;
                    case 4:
                        Menus.changeGlobalEnvironmentCurrency();
                        break;
                    case 5:
                        Menus.updateWalletBalance();
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

}