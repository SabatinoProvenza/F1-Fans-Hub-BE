package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.util.List;

public record Rss2JsonResponse(
        Feed feed,
        List<Item> items
) {

    public record Feed(
            String title
    ) {
    }

    public record Item(
            String title,
            String pubDate,
            String link,
            String guid,
            String author,
            String thumbnail,
            String description,
            String content,
            Enclosure enclosure
    ) {
    }

    public record Enclosure(
            String link, String type, Integer length
    ) {
    }
}
