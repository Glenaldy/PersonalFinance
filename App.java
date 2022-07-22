public class App {

    /**
     * THIS IS THE DRIVER OF THE APPLICATION
     * 
     * BEFORE RUNNING THE APPLICATION, PLEASE REFER TO THE GLOBALENVIRONMENTVARIABLE
     * TO CHANGE THE PATH
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        MainMenu.mainMenu(GlobalEnvironmentVariable.db);
    }
}