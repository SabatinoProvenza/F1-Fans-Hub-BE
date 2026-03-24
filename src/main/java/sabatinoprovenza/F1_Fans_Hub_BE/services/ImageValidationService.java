package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
public class ImageValidationService {

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    public void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }

        validateContentType(file);
        validateExtension(file);
        validateSize(file);
        validateImageContent(file);
    }

    private void validateContentType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Puoi caricare solo immagini JPG, PNG o GIF");
        }
    }

    private void validateExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || !originalFilename.matches("(?i).+\\.(jpg|jpeg|png|gif)$")) {
            throw new BadRequestException("Estensione file non valida. Sono consentiti solo JPG, JPEG, PNG e GIF");
        }
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("L'immagine non può superare i 5 MB");
        }
    }

    private void validateImageContent(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BadRequestException("Il file caricato non è un'immagine valida");
            }
        } catch (IOException e) {
            throw new BadRequestException("Errore nella lettura del file immagine");
        }
    }
}
