package sabatinoprovenza.F1_Fans_Hub_BE.exceptions;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
