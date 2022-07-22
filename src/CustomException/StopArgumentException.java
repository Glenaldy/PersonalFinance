package CustomException;

/**
 * Custom exceptions that indicates that the menus in the Main Menu will stop
 * running
 */
public class StopArgumentException extends Exception {
    public StopArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
