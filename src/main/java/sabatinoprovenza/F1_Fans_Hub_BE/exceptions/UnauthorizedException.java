package sabatinoprovenza.F1_Fans_Hub_BE.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
