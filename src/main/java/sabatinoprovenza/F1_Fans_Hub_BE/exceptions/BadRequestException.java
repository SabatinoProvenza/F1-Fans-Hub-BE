package sabatinoprovenza.F1_Fans_Hub_BE.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
