package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {
}
