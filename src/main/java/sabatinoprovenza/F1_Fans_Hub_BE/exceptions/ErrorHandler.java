package sabatinoprovenza.F1_Fans_Hub_BE.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ErrorsDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleValidationException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .toList();

        return new ErrorsDTO(
                "Errori nel payload",
                LocalDateTime.now(),
                errors
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleUnauthorizedException(UnauthorizedException ex) {

        
        return new ErrorsDTO(
                ex.getMessage(),
                LocalDateTime.now(),
                List.of(ex.getMessage())
        );
    }
}
