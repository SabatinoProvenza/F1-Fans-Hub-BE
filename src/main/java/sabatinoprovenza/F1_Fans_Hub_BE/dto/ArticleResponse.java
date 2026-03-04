package sabatinoprovenza.F1_Fans_Hub_BE.dto;

public record ArticleResponse(
        String id,
        String title,
        String description,
        String image,
        String link,
        String pubDate,
        String source
) {
}
