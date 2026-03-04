package sabatinoprovenza.F1_Fans_Hub_BE.services;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.Rss2JsonResponse;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class NewsService {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<ArticleResponse> getNews() throws Exception {

        String url = "https://api.rss2json.com/v1/api.json?rss_url=https://it.motorsport.com/rss/f1/news/";

        HttpResponse<String> response = Unirest.get(url).asString();

        Rss2JsonResponse rss = mapper.readValue(response.getBody(), Rss2JsonResponse.class);

        return rss.items()
                .stream()
                .map(item -> new ArticleResponse(
                        item.guid() != null ? item.guid() : item.link(),
                        item.title(),
                        stripHtml(item.description()),
                        extractImage(item),
                        item.link(),
                        item.pubDate(),
                        rss.feed().title()
                ))
                .toList();
    }

    private String extractImage(Rss2JsonResponse.Item item) {
        if (item.enclosure() != null && item.enclosure().link() != null && !item.enclosure().link().isBlank()) {
            return item.enclosure().link();
        }
        if (item.thumbnail() != null && !item.thumbnail().isBlank()) {
            return item.thumbnail();
        }
        return "";
    }

    private String stripHtml(String html) {
        if (html == null) return "";

        // rimuove HTML
        String text = html.replaceAll("<[^>]*>", "");

        // rimuove "Continua a leggere"
        int index = text.indexOf("Continua a leggere");
        if (index != -1) {
            text = text.substring(0, index);
        }

        text = text.trim();

        // tronca a 160 caratteri
        int maxLength = 160;

        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "...";
        }

        return text;
    }
}
