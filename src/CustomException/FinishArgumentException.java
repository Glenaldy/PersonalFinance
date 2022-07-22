package CustomException;

/**
 * Custom exceptions that indicates that the programm wills top running
 */
public class FinishArgumentException extends Exception {
    public FinishArgumentException(String errorMessage) {
        super(errorMessage);
    }
}