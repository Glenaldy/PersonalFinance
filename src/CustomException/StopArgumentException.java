package CustomException;

public class StopArgumentException extends Exception {
    public StopArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
