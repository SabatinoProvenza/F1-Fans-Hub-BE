package sabatinoprovenza.F1_Fans_Hub_BE.services;


import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;

import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsService {


    public List<ArticleResponse> getNews() {

        try {
            String feedUrl = "https://it.motorsport.com/rss/f1/news/";

            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            if (feed == null || feed.getEntries() == null || feed.getEntries().isEmpty()) {
                throw new RuntimeException("Nessun articolo trovato");
            }

            String source = (feed.getTitle() != null && !feed.getTitle().isBlank())
                    ? feed.getTitle().split(" - ")[0]
                    : "Motorsport";

            return feed.getEntries()
                    .stream()
                    .map(item -> new ArticleResponse(
                            null,
                            item.getUri() != null ? item.getUri() : item.getLink(),
                            item.getTitle() != null ? item.getTitle() : "",
                            stripHtml(getDescription(item)),
                            stripHtml(getContent(item)),
                            extractImage(item),
                            item.getLink() != null ? item.getLink() : "",
                            formatDate(item.getPublishedDate()),
                            source,
                            false
                    ))
                    .toList();

        } catch (Exception e) {
            System.out.println("Errore nel recupero delle news: " + e.getMessage());
            return List.of();
        }
    }

    private String extractImage(SyndEntry item) {
        if (item.getEnclosures() != null) {
            for (SyndEnclosure enclosure : item.getEnclosures()) {
                if (enclosure.getUrl() != null && !enclosure.getUrl().isBlank()) {
                    String type = enclosure.getType();
                    if (type == null || type.startsWith("image")) {
                        return enclosure.getUrl();
                    }
                }
            }
        }

        String html = getContent(item);
        String imageFromHtml = extractImageFromHtml(html);
        if (imageFromHtml != null && !imageFromHtml.isBlank()) {
            return imageFromHtml;
        }

        html = getDescription(item);
        imageFromHtml = extractImageFromHtml(html);
        if (imageFromHtml != null && !imageFromHtml.isBlank()) {
            return imageFromHtml;
        }

        return "";
    }

    private String getDescription(SyndEntry item) {
        if (item.getDescription() != null && item.getDescription().getValue() != null) {
            return item.getDescription().getValue();
        }
        return "";
    }

    private String getContent(SyndEntry item) {
        if (item.getContents() != null && !item.getContents().isEmpty()) {
            SyndContent content = item.getContents().get(0);
            if (content != null && content.getValue() != null) {
                return content.getValue();
            }
        }
        return "";
    }

    private String extractImageFromHtml(String html) {
        if (html == null || html.isBlank()) {
            return null;
        }

        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String stripHtml(String html) {
        if (html == null) return "";

        String text = html.replaceAll("<[^>]*>", "");

        int index = text.indexOf("Continua a leggere");
        if (index != -1) {
            text = text.substring(0, index);
        }

        return text.trim();
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public ArticleResponse getNewsByGuid(String guid) {
        return getNews().stream()
                .filter(a -> a.guid().equals(guid))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Articolo non trovato"));
    }
}

