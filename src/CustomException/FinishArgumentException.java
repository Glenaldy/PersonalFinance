package CustomException;

public class FinishArgumentException extends Exception {
    public FinishArgumentException(String errorMessage) {
        super(errorMessage);
    }
}